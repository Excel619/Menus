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
 * Represents a menu with multiple pages and handles logic for GUI elements and player interaction.
 * Page numbers start from zero.
 *
 * Size (from the super class) refers to the size of the first page only.
 * Menu#items and Menu#inventory will contain the first page of this menu.
 * Title is the title of the first page only.
 *
 * @constructor Create a populated PagedMenu
 *
 * @param menusAPI The owning MenusAPI object constructed for the plugin using this API
 * @property pages Wrappers for the items in each page of this menu
 * @param animations List of menu animations to run when a player opens this menu
 * @param interactionsBlocked Default value deciding if to allow players to move items in the inventory. Can be overwritten by MenuItems.
 * @param onClickHandler Lambda that runs on click anywhere in the GUI for any player
 * @param onCloseHandler Lambda that runs on menu close for any player
 */
class PagedMenu @JvmOverloads constructor(
    menusAPI: MenusAPI,
    private val pages: List<Page>,
    animations: List<MenuAnimation>,
    interactionsBlocked: Boolean = true,
    onClickHandler: (InventoryClickEvent) -> Unit = {},
    onCloseHandler: (InventoryCloseEvent) -> Unit = {}
): Menu(
    menusAPI,
    pages.firstOrNull()?.title ?: throw IllegalArgumentException("PagedMenu must have at least one page!"),
    pages.firstOrNull()?.items ?: throw IllegalArgumentException("PagedMenu must have at least one page!"),
    pages.firstOrNull()?.size ?: throw IllegalArgumentException("PagedMenu must have at least one page!"),
    animations,
    interactionsBlocked,
    onClickHandler,
    onCloseHandler
) {

    /** The GUIs backing this menu, each containing page's inventory */
    private var inventories = ArrayList<Inventory>()
    /** List of viewers of this menu, mapped to page number they are viewing. Used for turning pages. */
    private val viewerPages = HashMap<UUID, Int>()

    init {
        // If we have only one page, then the Menu init block will
        // apply default interactions blocked for first page (Menu#inventory)
        // Otherwise, loop through remaining pages
        if (pages.size > 1) for (pageNumber in 1 until pages.size) {
            MenuUtil.applyDefaultInteractionsBlocked(pages[pageNumber].items, interactionsBlocked)
        }

        // Since Menu#inventory represents this PagedMenu's first page, add it to our pages
        inventories.add(inventory)
        // Start from index one because super.fillInventory() filled the first page
        if (pages.size > 1) for (pageNumber in 1 until pages.size) {
            // Get page items for the current page
            val currentPage = pages[pageNumber]
            // Create an inventory using the static size for all pages in the menu
            val pageInventory =
                if (currentPage.title.isEmpty() || currentPage.title.isBlank()) Bukkit.createInventory(this, currentPage.size)
                else Bukkit.createInventory(this, currentPage.size, currentPage.title)
            // Fill menu
            for ((slot, item) in currentPage.items) pageInventory.setItem(slot, item.icon)
            // Add new page to list of pages
            inventories.add(pageInventory)
        }
    }

    /**
     * API note: WARNING - Do not use this to open a menu, instead use Menu#openMenu!
     *
     * Gets the first page's inventory that backs this menu. This should be used for observation and no code should modify this inventory.
     * Use getInventoryPage(Int) to get a specific page.
     */
    override fun getInventory(): Inventory {
        return inventories[0]
    }

    /**
     * API note: WARNING - Do not use this to open a menu, instead use Menu#openMenu!
     *
     * Gets a specific page's inventory that backs it. This should be used for observation and no code should modify this inventory.
     */
    fun getInventoryPage(page: Int): Inventory {
        if (!isValidPage(page)) throw IllegalArgumentException("Page $page does not exist in this menu!")
        return inventories[page]
    }

    /**
     * Gets a menu item in a specific page at a specific slot.
     *
     * @param slot MenuItem's slot
     * @param page MenuItem's page
     * @return MenuItem
     */
    fun getMenuItem(slot: Int, page: Int): MenuItem {
        if (!containsMenuItem(slot, page)) throw IllegalArgumentException("Menu does not contain item at slot $slot! Use containsMenuItem to check first.")
        return pages[page].items[slot]!!
    }

    /**
     * Checks if the menu contains a MenuItem at the given slot and page.
     *
     * @param slot MenuItem's slot
     * @param page MenuItem's page
     * @return Whether or not this menu contains an item at the specified slot
     */
    fun containsMenuItem(slot: Int, page: Int): Boolean {
        if (!isValidPage(page)) return false
        return pages[page].items.containsKey(slot)
    }

    /**
     * Opens the first page of this menu for a player.
     * This will not only send packets to the player with the inventory but also handle certain things server side to keep the menu working.
     *
     * @param player Menu viewer
     */
    override fun openMenu(player: Player) {
        openMenuPage(player, 0)
    }

    /**
     * Opens a specific page of this menu for a player.
     * This will not only send packets to the player with the inventory but also handle certain things server side to keep the menu working.
     *
     * @param player Menu viewer
     * @param page Menu page
     */
    fun openMenuPage(player: Player, page: Int) {
        if (!isValidPage(page)) throw IllegalArgumentException("Page $page does not exist in this menu!")
        player.openInventory(getInventoryPage(page))
        viewerPages[player.uniqueId] = page
    }

    /**
     * Checks if the given page number exists in this menu
     *
     * @param page Page number
     * @return Is valid page
     */
    fun isValidPage(page: Int) = page in 0 until inventories.size

    /**
     * Length of this menu, how many pages it has
     *
     * @return Pages size
     */
    fun size() = inventories.size

    override fun onClick(event: InventoryClickEvent) {
        // Run the on click handler
        onClickHandler(event)

        // Get the page we are viewing, if not viewing return
        val page = viewerPages[event.whoClicked.uniqueId] ?: return
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
        viewerPages.remove(event.player.uniqueId)
    }

    /**
     * Turns to the next page for a viewer of this menu.
     * The player given must be an active viewer.
     *
     * @param player Menu viewer
     */
    fun openNextPage(player: Player) {
        if (!viewerPages.containsKey(player.uniqueId)) throw IllegalStateException("Cannot open next page for PagedMenu if player is not viewing this menu!")
        val currentPage = viewerPages[player.uniqueId]!!
        val nextPage = if (isValidPage(currentPage + 1)) currentPage + 1 else currentPage
        openMenuPage(player, nextPage)
    }

    /**
     * Turns to the previous page for a viewer of this menu.
     * The player given must be an active viewer.
     *
     * @param player Menu viewer
     */
    fun openPreviousPage(player: Player) {
        if (!viewerPages.containsKey(player.uniqueId)) throw IllegalStateException("Cannot open next page for PagedMenu if player is not viewing this menu!")
        val currentPage = viewerPages[player.uniqueId]!!
        val previousPage = if (isValidPage(currentPage - 1)) currentPage - 1 else currentPage
        openMenuPage(player, previousPage)
    }


    /**
     * Wrapper for the items, size, and title of a page in a menu.
     *
     * @constructor Construct a populated menu page
     *
     * @property title The title of this page
     * @param items The items contained in the page, mapped slot to ItemStack wrapper, MenuItem
     * @param size Inventory size
     */
    class Page(var title: String, items: MutableMap<Int, MenuItem>, size: Int) {

        /** Items in this page */
        var items = items
            private set
        /** Inventory size of this page */
        var size = size
            set(size) {
                if (!MenuUtil.isValidMenuSize(size)) throw IllegalArgumentException("$size is not a valid bukkit menu size!")
                field = size
            }

        /** Construct an empty menu page */
        constructor(title: String, size: Int): this(title, HashMap<Int, MenuItem>(), size)

        init {
            if (size !in 0..54 || size % 9 != 0) throw IllegalArgumentException("MenuPageItems invalid page size: $size")
        }

        /**
         * Sets an item icon in this page.
         * Note that if the slot doesn't fit in the inventory's current size, the inventory will be expanded.
         *
         * @param slot Item slot
         * @param item MenuItem ItemStack wrapper
         */
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

        /**
         * Checks if this inventory is full, meaning that if we didn't expand the inventory at all, there are no more slots.
         *
         * @return Is full
         */
        fun isFull(): Boolean = items.size == size

        /**
         * Gets the first empty slot in this inventory, using the MenuUtil.
         *
         * @return first empty slot
         *
         * @see com.gmail.excel8392.menus.util.MenuUtil.getFirstEmptySlot
         */
        fun getFirstEmptySlot(): Int = MenuUtil.getFirstEmptySlot(items, size)

    }

}