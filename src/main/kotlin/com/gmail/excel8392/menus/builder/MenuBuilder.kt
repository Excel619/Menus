package com.gmail.excel8392.menus.builder

import com.gmail.excel8392.menus.animation.MenuAnimation
import com.gmail.excel8392.menus.menu.Menu
import com.gmail.excel8392.menus.menu.MenuItem
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

/**
 * Interface representing a Builder Pattern for creating a Menu.
 */
interface MenuBuilder: Cloneable {

    fun setItem(slot: Int, menuItem: MenuItem): MenuBuilder

    fun setItem(slot: Int, item: ItemStack): MenuBuilder = setItem(slot, MenuItem(item))

    fun addItem(menuItem: MenuItem): MenuBuilder

    fun addItem(item: ItemStack): MenuBuilder = addItem(MenuItem(item))

    fun addAnimation(menuAnimation: MenuAnimation): MenuBuilder

    fun setInteractionsBlocked(interactionsBlocked: Boolean): MenuBuilder

    fun setOnClose(onClose: (InventoryCloseEvent) -> Unit): MenuBuilder

    fun setOnClick(onClick: (InventoryClickEvent) -> Unit): MenuBuilder

    fun build(): Menu

    override fun clone(): MenuBuilder

}