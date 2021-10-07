package com.gmail.excel8392.menus.builder

import com.gmail.excel8392.menus.animation.MenuAnimation
import com.gmail.excel8392.menus.menu.Menu
import com.gmail.excel8392.menus.menu.MenuItem
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

/**
 * Interface representing a Builder Pattern for creating a Menu.
 * Can be cloned and will create another menu builder with the same items and animation objects.
 */
interface MenuBuilder: Cloneable {

    /**
     * Set an item with a specific slot and MenuItem.
     *
     * @param slot The slot to insert the icon at.
     * @param menuItem The MenuItem to represent this slot
     * @return This builder for use in the builder pattern
     */
    fun setItem(slot: Int, menuItem: MenuItem): MenuBuilder

    /**
     * Set an item with a specific slot and ItemStack.
     * The ItemStack icon is immediately used to generate a MenuItem without any actions.
     *
     * @param slot The slot to insert the icon at.
     * @param item The ItemStack icon to represent this slot
     * @return This builder for use in the builder pattern
     */
    fun setItem(slot: Int, item: ItemStack) = setItem(slot, MenuItem(item))

    /**
     * Set an item with a specific slot and MenuItem.
     * The MenuItemBuilder is immediately used to build a MenuItem.
     *
     * @param slot The slot to insert the icon at.
     * @param itemBuilder The MenuItem to represent this slot
     * @return This builder for use in the builder pattern
     */
    fun setItem(slot: Int, itemBuilder: MenuItemBuilder): MenuBuilder = setItem(slot, itemBuilder.build())

    /**
     * Add an item to the first available slot in the menu.
     *
     * @param menuItem The MenuItem to represent this slot
     * @return This builder for use in the builder pattern
     */
    fun addItem(menuItem: MenuItem): MenuBuilder

    /**
     * Add an item to the first available slot in the menu.
     * The ItemStack icon is immediately used to generate a MenuItem without any actions.
     *
     * @param item The ItemStack icon to represent this slot
     * @return This builder for use in the builder pattern
     */
    fun addItem(item: ItemStack): MenuBuilder = addItem(MenuItem(item))

    /**
     * Add a menu animation for this menu.
     *
     * @param menuAnimation The animation to add
     */
    fun addAnimation(menuAnimation: MenuAnimation): MenuBuilder

    /**
     * Set whether or not this menu should allow players to take and put items inside it.
     */
    fun setInteractionsBlocked(interactionsBlocked: Boolean): MenuBuilder

    fun setOnClose(onClose: (InventoryCloseEvent) -> Unit): MenuBuilder

    fun setOnClick(onClick: (InventoryClickEvent) -> Unit): MenuBuilder

    fun build(): Menu

    override fun clone(): MenuBuilder

    // TODO fun border

}