package com.gmail.excel8392.menus.action

import com.gmail.excel8392.menus.PagedMenusManager
import com.gmail.excel8392.menus.menu.PagedMenu
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class ChangePageAction(val type: Type): Action {

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

    enum class Type {

        NEXT_PAGE, PREVIOUS_PAGE, FIRST_PAGE, LAST_PAGE;

    }

}