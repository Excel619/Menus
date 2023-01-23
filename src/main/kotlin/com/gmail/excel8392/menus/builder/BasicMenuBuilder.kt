package com.gmail.excel8392.menus.builder

import com.gmail.excel8392.menus.MenusAPI
import com.gmail.excel8392.menus.action.MenuAction
import com.gmail.excel8392.menus.animation.MenuAnimation
import com.gmail.excel8392.menus.menu.Menu
import com.gmail.excel8392.menus.menu.MenuItem
import com.gmail.excel8392.menus.util.MenuUtil
import org.bukkit.ChatColor
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import java.util.LinkedList

/**
 * Basic implementation of the MenuBuilder.
 * This is likely the builder you will want to use for most menus.
 *
 * @constructor Create an empty basic menu builder
 *
 * @property menusAPI The owning MenusAPI object constructed for the plugin using this API
 * @property title The title of the menu at the top (supports color codes)
 * @property size The size of the menu, size is between 9 and 54 and is a multiple of 9
 * @property colorPrefix Color code prefix for translating alternate color codes
 *
 * @see com.gmail.excel8392.menus.builder.MenuBuilder
 * @see com.gmail.excel8392.menus.menu.Menu
 */
class BasicMenuBuilder @JvmOverloads internal constructor(
    val menusAPI: MenusAPI,
    var title: String,
    override var size: Int,
    private var colorPrefix: Char = '&'
): MenuBuilder<BasicMenuBuilder> {

    /** Mapping of slots in the inventory to items **/
    private var items: MutableMap<Int, MenuItem> = HashMap()
    /** List of animations for this menu */
    private var animations: MutableList<MenuAnimation> = LinkedList<MenuAnimation>()
    /** Default interactions blocked value */
    private var interactionsBlocked = true
    /** Default on close handler */
    private var onClose: (InventoryCloseEvent) -> Unit = {}
    /** Default on click handler */
    private var onClick: (InventoryClickEvent) -> Unit = {}

    /** List of the on click handlers for each specific slot **/
    private var slotActions: MutableMap<Int, MenuAction> = HashMap()

    init {
        title = ChatColor.translateAlternateColorCodes(colorPrefix, title)
        if (!MenuUtil.isValidMenuSize(size)) throw IllegalArgumentException("$size is not a valid bukkit menu size!")
    }

    /**
     * Construct this BasicMenuBuilder with no title set.
     *
     * @param menusAPI The owning MenusAPI object constructed for the plugin using this API
     * @param size The size of the menu, size is between 9 and 54 and is a multiple of 9
     * @param colorPrefix Color code prefix for translating alternate color codes
     */
    @JvmOverloads
    internal constructor(menusAPI: MenusAPI, size: Int, colorPrefix: Char = '&'): this(menusAPI, "", size, colorPrefix = colorPrefix)

    override fun setItem(slot: Int, menuItem: MenuItem): BasicMenuBuilder {
        if (slot !in 0 until size) throw IllegalArgumentException("Cannot insert MenuItem at slot $slot because it is greater than the menu size of $size")
        items[slot] = menuItem
        return this
    }

    override fun addItem(menuItem: MenuItem): BasicMenuBuilder {
        items[MenuUtil.getFirstEmptySlot(items, size)] = menuItem
        return this
    }

    override fun removeItem(slot: Int): BasicMenuBuilder {
        items.remove(slot)
        return this
    }

    override fun addAnimation(menuAnimation: MenuAnimation): BasicMenuBuilder {
        animations.add(menuAnimation)
        return this
    }

    /**
     * Set the list of animations for this menu. Deletes all animations currently in the list.
     *
     * @param animations List of animations for this menu
     * @return This builder for use in the builder pattern
     */
    fun setAnimations(animations: List<MenuAnimation>): BasicMenuBuilder {
        this.animations = if (animations is MutableList<MenuAnimation>) animations else LinkedList(animations)
        return this
    }

    /**
     * Set the map between inventory slots and item for this menu.
     * This does not add the items, but rather disregards all changes made up until now
     * and replaces them with the new map.
     *
     * @param items Map of each inventory slot to an item
     * @return This builder for use in the builder pattern
     */
    fun setItems(items: MutableMap<Int, MenuItem>): BasicMenuBuilder {
        if (items.size > size) {
            val newSize = MenuUtil.getMinSlots(items.keys)
            if (!MenuUtil.isValidMenuSize(newSize)) throw IllegalArgumentException("Cannot accept items map: too large!")
            size = newSize
        }
        this.items = items
        return this
    }

    override fun setInteractionsBlocked(interactionsBlocked: Boolean): BasicMenuBuilder {
        this.interactionsBlocked = interactionsBlocked
        return this
    }

    override fun setOnClose(onClose: (InventoryCloseEvent) -> Unit): BasicMenuBuilder {
        this.onClose = onClose
        return this
    }

    override fun setOnClick(onClick: (InventoryClickEvent) -> Unit): BasicMenuBuilder {
        this.onClick = onClick
        return this
    }

    /**
     * Sets the action to perform upon clicking the icon in a given slot.
     *
     * @param slot Slot to click on
     * @param action Action to perform
     * @return This builder for use in the builder pattern
     */
    fun setSlotAction(slot: Int, action: MenuAction): BasicMenuBuilder {
        slotActions[slot] = action
        return this
    }

    /**
     * Set the map of slots to their actions on click. For use when cloning this menu builder.
     *
     * @param slotActions Mapping of slots to actions
     * @return This builder for use in the builder pattern
     */
    fun setSlotActions(slotActions: MutableMap<Int, MenuAction>): BasicMenuBuilder {
        this.slotActions = slotActions
        return this
    }

    override fun build(): Menu {
        for ((slot, action) in slotActions.entries) items[slot]?.addAction(action)
        return Menu(
            menusAPI,
            title,
            items,
            size,
            animations,
            interactionsBlocked = interactionsBlocked,
            onCloseHandler = onClose,
            onClickHandler = onClick
        )
    }

    override fun clone(): BasicMenuBuilder {
        val newItems = HashMap<Int, MenuItem>()
        for ((slot, menuItem) in items) newItems[slot] = menuItem
        return BasicMenuBuilder(menusAPI, title, size, colorPrefix = colorPrefix)
            .setItems(newItems)
            .setAnimations(animations)
            .setInteractionsBlocked(interactionsBlocked)
            .setOnClose(onClose)
            .setOnClick(onClick)
            .setSlotActions(slotActions)
    }

}