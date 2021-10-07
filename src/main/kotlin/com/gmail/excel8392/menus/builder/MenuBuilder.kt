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

    val size: Int

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

    enum class MenuBuilderBorder {
        TOP, BOTTOM, LEFT, RIGHT
    }

    fun addBorder(borderItem: ItemStack, vararg borders: MenuBuilderBorder) {
        for (borderSide in borders) {
            when (borderSide) {
                // TODO needs updating when dropper menus exist
                MenuBuilderBorder.TOP -> for (i in 0 until 9) setItem(i, borderItem)
                MenuBuilderBorder.BOTTOM -> for (i in 0 until 9) setItem(size - i - 1, borderItem)
                MenuBuilderBorder.LEFT -> for (i in 0 until size step 9) setItem(i, borderItem)
                MenuBuilderBorder.RIGHT -> for (i in 0 until size step 9) setItem(i + 8, borderItem)
            }
        }
    }

    fun addBorder(borderItem: ItemStack) = addBorder(borderItem, *MenuBuilderBorder.values())

    fun build(): Menu

    override fun clone(): MenuBuilder

}