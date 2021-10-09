package com.gmail.excel8392.menus.menu

import com.gmail.excel8392.menus.MenusAPI
import com.gmail.excel8392.menus.PagedMenusManager
import com.gmail.excel8392.menus.animation.MenuAnimation
import com.gmail.excel8392.menus.util.MenuUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory

/**
 * TODO
 *
 * Size refers to the size of the first page only.
 * Menu#items and Menu#inventory will contain the first page of this menu.
 */
class PagedMenu @JvmOverloads constructor(
    menusAPI: MenusAPI,
    title: String,
    val pageItems: List<PageItems>,
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

    // TODO make it so that this class handles page turning, rather than PagedMenusManager

    private var pages = ArrayList<Inventory>(pageItems.size)

    init {
        // If we have only one page, then the Menu init block will
        // apply default interactions blocked for first page (Menu#inventory)
        // Otherwise, loop through remaining pages
        if (pageItems.size > 1) for (pageNumber in 1 until pageItems.size) {
            MenuUtil.applyDefaultInteractionsBlocked(pageItems[pageNumber].items, interactionsBlocked)
        }
    }

    override fun fillInventory() {
        // Use the super method to fill in the Menu#inventory with Menu#items
        super.fillInventory()
        // Since Menu#inventory represents this PagedMenu's first page, add it to our pages
        pages.add(inventory)
        // Start from index one because super.fillInventory() filled the first page
        if (pageItems.size > 1) for (pageNumber in 1 until pageItems.size) {
            // Receive
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

    override fun openMenu(player: Player) {
        PagedMenusManager.openPagedMenu(player, this)
    }

    fun openMenuPage(player: Player, page: Int) {
        if (!isValidPage(page)) throw IllegalArgumentException("Page $page does not exist in this menu!")
        PagedMenusManager.openPagedMenu(player, this, page)
    }

    fun isValidPage(page: Int) = page in 0 until pages.size

    fun size() = pages.size


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
            if (slot >= size) {
                try {
                    size = MenuUtil.getMinSlots(items.keys)
                } catch (exception: IllegalArgumentException) {
                    throw IllegalArgumentException("Cannot add item to menu, menu is at max size!")
                }
            }
            items[slot] = item
        }

        fun isFull(): Boolean = items.size == size

        fun getFirstEmptySlot(): Int = MenuUtil.getFirstEmptySlot(items, size)

    }

}