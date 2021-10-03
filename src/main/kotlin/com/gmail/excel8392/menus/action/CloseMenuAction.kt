package com.gmail.excel8392.menus.action

import org.bukkit.event.inventory.InventoryClickEvent

/**
 * {@inheritDoc}
 *
 * Action for MenuItems that closes the menu the player is viewing upon execute.
 *
 * @see com.gmail.excel8392.menus.action.Action
 * @see com.gmail.excel8392.menus.menu.MenuItem
 * @see com.gmail.excel8392.menus.builder.MenuItemBuilder
 * @see com.gmail.excel8392.menus.menu.Menu
 */
class CloseMenuAction: Action {

    /** {@inheritDoc} */
    override fun execute(event: InventoryClickEvent) = event.whoClicked.closeInventory()

}