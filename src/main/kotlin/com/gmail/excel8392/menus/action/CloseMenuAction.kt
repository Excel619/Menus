package com.gmail.excel8392.menus.action

import com.gmail.excel8392.menus.menu.Menu
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * Action for MenuItems that closes the menu the player is viewing upon execute.
 * Actions are fired on menu item click.
 *
 * @see com.gmail.excel8392.menus.action.Action
 * @see com.gmail.excel8392.menus.menu.MenuItem
 */
class CloseMenuAction: Action {

    /** Handles this action executing upon the click of a MenuItem icon in a Menu.
     * Any changes made to the InventoryClickEvent parameter will be applied to the bukkit event.
     *
     * This closes the current open menu the player is viewing. If it is not a menu, it does nothing.
     *
     * @param event The bukkit event that is calling for the execute of this action
     */
    override fun execute(event: InventoryClickEvent) {
        if (event.inventory.holder is Menu) event.whoClicked.closeInventory()
    }

}