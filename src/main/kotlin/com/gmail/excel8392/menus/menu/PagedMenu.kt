package com.gmail.excel8392.menus.menu

import com.gmail.excel8392.menus.MenusAPI
import com.gmail.excel8392.menus.animation.MenuAnimation
import com.gmail.excel8392.menus.util.MenuUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import java.util.UUID

/**
 * TODO
 *
 * Size refers to the size of the first page only.
 * Menu#items and Menu#inventory will contain the first page of this menu.
 */
class PagedMenu @JvmOverloads constructor(
    menusAPI: MenusAPI,
    title: String,
    private val pageItems: List<PageItems>,
    animations: List<MenuAnimation>,
    interactionsBlocked: Boolean = true,
    onClick: (InventoryClickEvent) -> Unit = {},
    onClose: (InventoryCloseEvent) -> Unit = {}
): Menu(
    menusAPI,
    title,
    pageItems.firstOrNull()?.items ?: throw IllegalArgumentException("PagedMenu must have at least one page!"),
    pageItems.firstOrNull()?.size ?: throw IllegalArgumentException("PagedMenu must have at least one page!"),
    animations,
    interactionsBlocked,
    onClick,
    onClose
) {

    private var pages = ArrayList<Inventory>()
    private val viewers = HashMap<UUID, Int>()

    init {
        // If we have only one page, then the Menu init block will
        // apply default interactions blocked for first page (Menu#inventory)
        // Otherwise, loop through remaining pages
        if (pageItems.size > 1) for (pageNumber in 1 until pageItems.size) {
            MenuUtil.applyDefaultInteractionsBlocked(pageItems[pageNumber].items, interactionsBlocked)
        }

        // Since Menu#inventory represents this PagedMenu's first page, add it to our pages
        pages.add(inventory)
        // Start from index one because super.fillInventory() filled the first page
        if (pageItems.size > 1) for (pageNumber in 1 until pageItems.size) {
            // Get page items for the current page
            val currentPage = pageItems[pageNumber]
            // Create an inventory using the static size for all pages in the menu
            val pageInventory =
                if (title.isEmpty() || title.isBlank()) Bukkit.createInventory(this, currentPage.size)
                else Bukkit.createInventory(this, currentPage.size, title)
            // Fill menu
            for ((slot, item) in currentPage.items) pageInventory.setItem(slot, item.icon)
            // Add new page to list of pages
            pages.add(pageInventory)
        }
    }

    /**
     * TODO
     */
    override fun getInventory(): Inventory {
        return pages[0]
    }

    /**
     *
     */
    fun getInventoryPage(page: Int): Inventory {
        if (!isValidPage(page)) throw IllegalArgumentException("Page $page does not exist in this menu!")
        return pages[page]
    }

    fun getMenuItem(slot: Int, page: Int): MenuItem {
        if (!containsMenuItem(slot, page)) throw IllegalArgumentException("Menu does not contain item at slot $slot! Use containsMenuItem to check first.")
        return pageItems[page].items[slot]!!
    }

    fun containsMenuItem(slot: Int, page: Int): Boolean {
        if (!isValidPage(page)) return false
        return pageItems[page].items.containsKey(slot)
    }

    override fun openMenu(player: Player) {
        openMenuPage(player, 0)
    }

    fun openMenuPage(player: Player, page: Int) {
        if (!isValidPage(page)) throw IllegalArgumentException("Page $page does not exist in this menu!")
        player.openInventory(getInventoryPage(page))
        viewers[player.uniqueId] = page
    }

    fun isValidPage(page: Int) = page in 0 until pages.size

    fun size() = pages.size

    override fun onClick(event: InventoryClickEvent) {
        // Run the on click handler
        onClickHandler(event)

        // Get the page we are viewing, if not viewing return
        val page = viewers[event.whoClicked.uniqueId] ?: return
        // Check that there is a menu item in the slot
        if (!containsMenuItem(event.slot, page)) return

        // Get the MenuItem clicked and execute actions, apply interactions blocked
        val clickedMenuItem = getMenuItem(event.slot, page)
        event.isCancelled = clickedMenuItem.interactionsBlocked
        // Fire the on click event for the menu item
        clickedMenuItem.onClick(event, this)
    }

    override fun onClose(event: InventoryCloseEvent) {
        super.onClose(event)
        viewers.remove(event.player.uniqueId)
    }

    fun openNextPage(player: Player) {
        if (!viewers.containsKey(player.uniqueId)) throw IllegalStateException("Cannot open next page for PagedMenu if player is not viewing this menu!")
        val currentPage = viewers[player.uniqueId]!!
        val nextPage = if (isValidPage(currentPage + 1)) currentPage + 1 else currentPage
        openMenuPage(player, nextPage)
    }

    fun openPreviousPage(player: Player) {
        if (!viewers.containsKey(player.uniqueId)) throw IllegalStateException("Cannot open next page for PagedMenu if player is not viewing this menu!")
        val currentPage = viewers[player.uniqueId]!!
        val previousPage = if (isValidPage(currentPage - 1)) currentPage - 1 else currentPage
        openMenuPage(player, previousPage)
    }


    class PageItems(items: MutableMap<Int, MenuItem>, size: Int) {

        var items = items
            private set
        var size = size
            set(size) {
                if (!MenuUtil.isValidMenuSize(size)) throw IllegalArgumentException("$size is not a valid bukkit menu size!")
                field = size
            }

        constructor(size: Int): this(HashMap<Int, MenuItem>(), size)

        init {
            if (size !in 0..54 || size % 9 != 0) throw IllegalArgumentException("MenuPageItems invalid page size: $size")
        }

        fun setItem(slot: Int, item: MenuItem) {
            items[slot] = item
            if (slot >= size) {
                try {
                    size = MenuUtil.getMinSlots(items.keys)
                } catch (exception: IllegalArgumentException) {
                    throw IllegalArgumentException("Cannot add item to menu, menu is at max size!")
                }
            }
        }

        fun isValidSlot(slot: Int) = slot >= size && size == 54

        fun isFull(): Boolean = items.size == size

        fun getFirstEmptySlot(): Int = MenuUtil.getFirstEmptySlot(items, size)

    }

}