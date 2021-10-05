package com.gmail.excel8392.menus

import com.gmail.excel8392.menus.menu.PagedMenu
import java.lang.IllegalStateException
import java.util.UUID
import kotlin.collections.HashMap
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerQuitEvent

object PagedMenusManager: Listener {

    private val menuPages = HashMap<UUID, Int>()

    @JvmStatic
    @JvmOverloads
    fun openPagedMenu(player: Player, menu: PagedMenu, page: Int = 0) {
        player.openInventory(menu.getInventoryPage(page))
        menuPages[player.uniqueId] = page
    }

    @JvmStatic
    fun openNextPage(player: Player, menu: PagedMenu) {
        if (!menuPages.containsKey(player.uniqueId)) throw IllegalStateException("Cannot open next page for PagedMenu if player is not viewing this menu!")
        val currentPage = menuPages[player.uniqueId]!!
        val nextPage = if (menu.isValidPage(currentPage + 1)) currentPage + 1 else currentPage
        openPagedMenu(player, menu, nextPage)
    }

    @JvmStatic
    fun openPreviousPage(player: Player, menu: PagedMenu) {
        if (!menuPages.containsKey(player.uniqueId)) throw IllegalStateException("Cannot open next page for PagedMenu if player is not viewing this menu!")
        val currentPage = menuPages[player.uniqueId]!!
        val previousPage = if (menu.isValidPage(currentPage - 1)) currentPage - 1 else currentPage
        openPagedMenu(player, menu, previousPage)
    }

    @EventHandler(priority = EventPriority.HIGH) // Fire after menu's onClose in MenusListener
    fun onInventoryClose(event: InventoryCloseEvent) {
        menuPages.remove(event.player.uniqueId)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        menuPages.remove(event.player.uniqueId)
    }

}