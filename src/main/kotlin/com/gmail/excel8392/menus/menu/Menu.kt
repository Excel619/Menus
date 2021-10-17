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
    protected var inventory: Inventory =
        if (title.isEmpty() || title.isBlank()) Bukkit.createInventory(this, size)
        else Bukkit.createInventory(this, size, title)
    @JvmName("getRawInventory") get // This is to avoid declaration clash with interface method

    protected val sortedAnimations = HashMap<Int, MutableList<MenuAnimation>>()
    protected val animationInterval: Long

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
     * TODO
     */
    @Warning(reason = "Do not use for opening this menu")
    override fun getInventory() = inventory

    open fun getMenuItem(slot: Int): MenuItem {
        if (!containsMenuItem(slot)) throw IllegalArgumentException("Menu does not contain item at slot $slot! Use containsMenuItem to check first.")
        return items[slot]!!
    }

    open fun containsMenuItem(slot: Int) = items.containsKey(slot)

    open fun openMenu(player: Player) {
        player.openInventory(inventory)
        beginAnimation(player)
    }

    protected open fun beginAnimation(player: Player) {
        Bukkit.getScheduler().runTaskTimer(menusAPI.plugin, Runnable {
            runningAnimations[player.uniqueId] = RunningAnimations(player)
            runningAnimations[player.uniqueId]!!.start()
        }, 0L, animationInterval)
    }

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

    open fun onClose(event: InventoryCloseEvent) {
        // Run the on close handler
        onCloseHandler(event)
        // Stop any running animations for this player
        if (runningAnimations.containsKey(event.player.uniqueId)) runningAnimations[event.player.uniqueId]!!.stop()
        runningAnimations.remove(event.player.uniqueId)
    }

    internal inner class RunningAnimations(val player: Player) {

        private val intervalCounter = HashMap<MenuAnimation, Long>()

        private var task: BukkitTask? = null
        private var hasRun = false

        init {
            for (animation in animations) {
                intervalCounter[animation] = 0
            }
        }

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

        fun stop() {
            task ?: throw IllegalStateException("Cannot stop RunningAnimations because it has not been started!")
            if (!task!!.isCancelled) task!!.cancel()
            task = null // Discard of the BukkitTask
        }

    }

}