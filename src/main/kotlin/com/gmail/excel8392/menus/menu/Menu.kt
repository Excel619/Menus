package com.gmail.excel8392.menus.menu

import com.gmail.excel8392.menus.MenusAPI
import com.gmail.excel8392.menus.animation.MenuAnimation
import com.gmail.excel8392.menus.util.MenuUtil
import com.gmail.excel8392.menus.util.NumberUtil
import org.bukkit.Bukkit
import org.bukkit.Warning
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.scheduler.BukkitTask
import java.util.LinkedList
import java.util.UUID

/**
 * Represents a menu that is created and handles logic for GUI elements and player interaction.
 * To open the menu, use openMenu(Player).
 *
 * @constructor Create a populated Menu
 *
 * @property menusAPI The owning MenusAPI object constructed for the plugin using this API
 * @property title The title of this menu
 * @property items This items this menu contains, mapping between slots and an ItemStack wrapper, MenuItem
 * @property size The inventory size for this menu, size must be between 9 and 54 and be a multiple of 9
 * @property animations List of menu animations to run when a player opens this menu
 * @property interactionsBlocked Default value deciding if to allow players to move items in the inventory. Can be overwritten by MenuItems.
 * @property onClickHandler Lambda that runs on click anywhere in the GUI for any player
 * @property onCloseHandler Lambda that runs on menu close for any player
 */
open class Menu @JvmOverloads constructor(
    val menusAPI: MenusAPI,
    var title: String,
    private val items: Map<Int, MenuItem>,
    var size: Int = 0,
    val animations: List<MenuAnimation>,
    val interactionsBlocked: Boolean = true,
    protected val onClickHandler: (InventoryClickEvent) -> Unit = {},
    protected val onCloseHandler: (InventoryCloseEvent) -> Unit = {}
): InventoryHolder {

    // It should be noted that we are leaking "this" in the Bukkit.createInventory,
    // but this is of no concern because it does not use the InventoryHolder
    /** The GUI backing this menu, containing all of its items */
    protected var inventory: Inventory =
        if (title.isEmpty() || title.isBlank()) Bukkit.createInventory(this, size)
        else Bukkit.createInventory(this, size, title)
    @JvmName("getRawInventory") get // This is to avoid declaration clash with interface method

    /** Animations for this menu sorted by interval length */
    protected val sortedAnimations = HashMap<Int, MutableList<MenuAnimation>>()
    /** Largest common denominator between all of the animations, the single interval for all of them */
    protected val animationInterval: Long

    /** Map between each player with the menu open and the menu animations running for them */
    internal val runningAnimations = HashMap<UUID, RunningAnimations>()

    init {
        // Set size to a valid inventory size (between 0-54, multiple of 9)
        if (size !in 1..54 || size % 9 != 0) size = MenuUtil.getMinSlots(items.keys)
        if (items.size > 54 || items.size > size) throw IllegalArgumentException("Menu cannot have more items than determined GUI size!")

        // Fill the inventory
        for ((slot, item) in items) inventory.setItem(slot, item.icon)
        // For all of the menu items in the inventory that don't have interactionsBlocked set, apply this menu's default
        MenuUtil.applyDefaultInteractionsBlocked(items, interactionsBlocked)

        // Sort all animations into a map for easier logic down the road
        for (animation in animations) {
            if (!sortedAnimations.containsKey(animation.interval)) sortedAnimations[animation.interval] = LinkedList()
            sortedAnimations[animation.interval]!!.add(animation)
        }
        // This is the interval we will use as the lowest possible interval to cover all animation intervals
        animationInterval = NumberUtil.gcd(sortedAnimations.keys).toLong()
    }

    /**
     * API note: WARNING - Do not use this to open a menu, instead use Menu#openMenu!
     *
     * Gets the inventory that backs this menu. This should be used for observation and no code should modify this inventory.
     */
    @Warning(reason = "Do not use for opening this menu")
    override fun getInventory() = inventory

    /**
     * Gets a MenuItem in this menu at a specific slot.
     *
     * @param slot MenuItem's slot
     * @return MenuItem
     */
    open fun getMenuItem(slot: Int): MenuItem {
        if (!containsMenuItem(slot)) throw IllegalArgumentException("Menu does not contain item at slot $slot! Use containsMenuItem to check first.")
        return items[slot]!!
    }

    /**
     * Checks if this menu contains a MenuItem at the given slot.
     *
     * @param slot MenuItem's slot
     * @return Whether or not this menu contains an item at the specified slot
     */
    open fun containsMenuItem(slot: Int) = items.containsKey(slot)

    /**
     * Opens this menu for a player.
     * This will not only send packets to the player with the inventory but also handle certain things server side to keep the menu working.
     *
     * @param player Menu viewer
     */
    open fun openMenu(player: Player) {
        player.openInventory(inventory)
        beginAnimation(player)
    }

    /**
     * Begins the menu animations for this menu with a certain viewer.
     *
     * @param player Menu viewer
     */
    protected open fun beginAnimation(player: Player) {
        Bukkit.getScheduler().runTaskTimer(menusAPI.plugin, Runnable {
            runningAnimations[player.uniqueId] = RunningAnimations(player)
            runningAnimations[player.uniqueId]!!.start()
        }, 0L, animationInterval)
    }

    /**
     * Logic that executes on menu item click.
     *
     * @param event Bukkit event wrapper for item click
     */
    open fun onClick(event: InventoryClickEvent) {
        // Run the on click handler
        onClickHandler(event)

        // Check to see if there is a menu item in this slot
        if (!containsMenuItem(event.slot)) return

        // Get the MenuItem clicked and execute actions, apply interactions blocked
        val clickedMenuItem = getMenuItem(event.slot)
        event.isCancelled = clickedMenuItem.interactionsBlocked
        // Fire the on click event for the menu item
        clickedMenuItem.onClick(event, this)
    }

    /**
     * Logic that executes on menu close.
     *
     * @param event Bukkit event wrapper for inventory close
     */
    open fun onClose(event: InventoryCloseEvent) {
        // Run the on close handler
        onCloseHandler(event)
        // Stop any running animations for this player
        if (runningAnimations.containsKey(event.player.uniqueId)) runningAnimations[event.player.uniqueId]!!.stop()
        runningAnimations.remove(event.player.uniqueId)
    }

    /**
     * Wrapper for the menu animations currently running for a viewer of this menu.
     * Each instance is dynamically tied to an instance of a menu.
     * RunningAnimations objects become useless after they have been started and stopped for a viewer.
     *
     * @constructor Create empty Running animations
     *
     * @property player Menu Viewer
     */
    internal inner class RunningAnimations(val player: Player) {

        /** Counts up each animation's current interval, resets back to 0 on execute */
        private val intervalCounter = HashMap<MenuAnimation, Long>()

        /** The bukkit task responsible for the animation ticking */
        private var task: BukkitTask? = null
        /** Whether or not this animation has been run */
        private var hasRun = false

        init {
            for (animation in animations) intervalCounter[animation] = 0
        }

        /**
         * Start the animations.
         */
        fun start() {
            if (task != null || hasRun) throw IllegalStateException("Cannot start RunningAnimations because it has already been run before!")
            task = Bukkit.getScheduler().runTaskTimer(menusAPI.plugin, Runnable {
                for (animation in intervalCounter.keys) {
                    intervalCounter[animation] = intervalCounter[animation]!! + animationInterval
                    if (intervalCounter[animation]!! >= animation.interval) {
                        intervalCounter[animation] = intervalCounter[animation]!! - animation.interval
                        animation.tickAnimation(player, this@Menu)
                    }
                }
            }, 0, animationInterval)
            hasRun = true
        }

        /**
         * Stop the animations.
         */
        fun stop() {
            task ?: throw IllegalStateException("Cannot stop RunningAnimations because it has not been started!")
            if (!task!!.isCancelled) task!!.cancel()
            task = null // Discard the BukkitTask
        }

    }

}