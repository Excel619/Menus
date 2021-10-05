package com.gmail.excel8392.menus.action

import org.bukkit.event.inventory.InventoryClickEvent

/**
 * Action for MenuItems that closes the menu the player is viewing upon execute.
 * Actions are fired on menu item click.
 *
 * @see com.gmail.excel8392.menus.action.Action
 * @see com.gmail.excel8392.menus.menu.MenuItem
 */
class CloseMenuAction: Action {

    /** {@inheritDoc} */
    override fun execute(event: InventoryClickEvent) = event.whoClicked.closeInventory()

}