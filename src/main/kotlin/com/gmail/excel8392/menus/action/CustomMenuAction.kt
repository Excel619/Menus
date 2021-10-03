package com.gmail.excel8392.menus.action

import org.bukkit.event.inventory.InventoryClickEvent

/**
 * {@inheritDoc}
 *
 * Action for MenuItems that executes a custom handler for an InventoryClickEvent upon execute.
 *
 * @param eventHandler Lambda handler for this event
 *
 * @see com.gmail.excel8392.menus.action.Action
 * @see com.gmail.excel8392.menus.menu.MenuItem
 * @see com.gmail.excel8392.menus.builder.MenuItemBuilder
 * @see com.gmail.excel8392.menus.menu.Menu
 */
open class CustomMenuAction(private val eventHandler: (InventoryClickEvent) -> Unit): Action {

    /** {@inheritDoc} */
    override fun execute(event: InventoryClickEvent) = eventHandler(event)

}