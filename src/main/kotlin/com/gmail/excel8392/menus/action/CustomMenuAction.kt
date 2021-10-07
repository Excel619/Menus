package com.gmail.excel8392.menus.action

import com.gmail.excel8392.menus.menu.Menu
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * Action for MenuItems that executes a custom handler for an InventoryClickEvent upon execute.
 * Actions are fired on menu item click.
 *
 * @param eventHandler Lambda handler for this event
 *
 * @see com.gmail.excel8392.menus.action.MenuAction
 * @see com.gmail.excel8392.menus.menu.MenuItem
 * @see com.gmail.excel8392.menus.builder.MenuItemBuilder
 * @see com.gmail.excel8392.menus.menu.Menu
 */
open class CustomMenuAction(private val eventHandler: (InventoryClickEvent) -> Unit): MenuAction {

    /**
     * Handles this action executing upon the click of a MenuItem icon in a Menu.
     * Any changes made to the InventoryClickEvent parameter will be applied to the bukkit event.
     *
     * This action allows a developer to execute their own operations on the given InventoryClickEvent.
     *
     * @param event The bukkit event that is calling for the execute of this action
     */
    override fun execute(event: InventoryClickEvent, menu: Menu) = eventHandler(event)

}