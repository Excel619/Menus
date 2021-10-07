package com.gmail.excel8392.menus.action

import com.gmail.excel8392.menus.PagedMenusManager
import com.gmail.excel8392.menus.menu.PagedMenu
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * Action for MenuItems that closes the menu the player is viewing upon execute.
 * Actions are fired on menu item click.
 *
 * @see com.gmail.excel8392.menus.action.Action
 * @see com.gmail.excel8392.menus.menu.MenuItem
 * @see com.gmail.excel8392.menus.menu.PagedMenu
 */
class ChangePageAction(val type: Type): Action {

    /**
     * Handles this action executing upon the click of a MenuItem icon in a Menu.
     * Any changes made to the InventoryClickEvent parameter will be applied to the bukkit event.
     *
     * This changes the player's open PagedMenu's page, depending on the provided ChangePaceAction.Type for this object.
     *
     * @param event - The bukkit event that is calling for the execute of this action
     */
    override fun execute(event: InventoryClickEvent) {
        // Player object of who clicked
        val player = event.whoClicked as? Player ?: return
        // Cast to custom Menu
        val pagedMenu = event.inventory.holder as? PagedMenu ?: return

        // Open menu depending on action type
        when (type) {
            Type.NEXT_PAGE -> PagedMenusManager.openNextPage(player, pagedMenu)
            Type.PREVIOUS_PAGE -> PagedMenusManager.openPreviousPage(player, pagedMenu)
            Type.FIRST_PAGE -> PagedMenusManager.openPagedMenu(player, pagedMenu)
            Type.LAST_PAGE -> PagedMenusManager.openPagedMenu(player, pagedMenu, pagedMenu.size() - 1)
        }
    }

    /**
     * The type of page change to execute upon ChangePageAction fire.
     */
    enum class Type {

        /** Turn to the next page in the menu, or current page if this is the last page */
        NEXT_PAGE,
        /** Turn to the previous page in the menu, or current page if this is the last page */
        PREVIOUS_PAGE,
        /** Turn to the first page in the menu */
        FIRST_PAGE,
        /** Turn to the last page in the menu */
        LAST_PAGE;

    }

}