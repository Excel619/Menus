package com.gmail.excel8392.menus.builder

import com.gmail.excel8392.menus.MenusAPI
import com.gmail.excel8392.menus.animation.MenuAnimation
import com.gmail.excel8392.menus.menu.MenuItem
import com.gmail.excel8392.menus.menu.PagedMenu
import com.gmail.excel8392.menus.util.MenuUtil
import org.bukkit.ChatColor
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import java.util.LinkedList

/**
 * Implementation of the Menu Builder allowing for menus of multiple pages.
 * When setting items in specific pages, <b>page numbers start from 0!</b>
 *
 * Note that while it is required to provide a default size (for newly created pages),
 * each page can be of different size. In addition page titles can be modified per page.
 * In addition, page title supports %pagenumber% which will be replaced with the number for that page.
 *
 * @constructor Create an empty paged menu builder
 *
 * @property menusAPI The owning MenusAPI object constructed for the plugin using this API
 * @property defaultTitle The default title of the menu at the top (supports color codes)
 * @property defaultSize The default size for new pages, size is between 9 and 54 and is a multiple of 9
 * @property colorPrefix Color code prefix for translating alternate color codes
 *
 * @see com.gmail.excel8392.menus.builder.MenuBuilder
 * @see com.gmail.excel8392.menus.menu.PagedMenu
 */
class PagedMenuBuilder @JvmOverloads constructor(
    var menusAPI: MenusAPI,
    var defaultTitle: String,
    val defaultSize: Int,
    private var colorPrefix: Char = '&'
): MenuBuilder<PagedMenuBuilder> {

    companion object {
        /** Placeholder for the page number used in page titles */
        private const val PAGE_NUMBER_PLACEHOLDER = "%pagenumber%"
    }

    override var size = defaultSize

    /** List of each page in the menu, with each page's items */
    var pages: MutableList<PagedMenu.Page> = ArrayList()
        private set

    /**
     * Map with entries specifying items that are "static" (identical) across all menu pages.
     * The key represents an inventory slot, the entry is a pair of the menu item,
     * and an array of page numbers to ignore, that should not contain the static item.
     */
    private var staticItems = HashMap<Int, Pair<MenuItem, IntArray>>()
    /** List of menu animations that will run for this menu */
    private var animations: MutableList<MenuAnimation> = LinkedList<MenuAnimation>()
    /** The default menu value for blocking viewer interactions in the menu */
    private var interactionsBlocked = true
    /** Lambda to run on menu close */
    private var onClose: (InventoryCloseEvent) -> Unit = {}
    /** Lambda to run on click anywhere in the menu */
    private var onClick: (InventoryClickEvent) -> Unit = {}

    init {
        defaultTitle = ChatColor.translateAlternateColorCodes(colorPrefix, defaultTitle)
    }

    override fun setItem(slot: Int, menuItem: MenuItem): PagedMenuBuilder {
        setStaticItem(slot, menuItem)
        return this
    }

    override fun addItem(menuItem: MenuItem): PagedMenuBuilder {
        setStaticItem(MenuUtil.getFirstEmptySlot(pages[0].items, pages[0].size), menuItem)
        return this
    }

    override fun addAnimation(menuAnimation: MenuAnimation): PagedMenuBuilder {
        animations.add(menuAnimation)
        return this
    }

    /**
     * Set the list of animations for this menu. Deletes all animations currently in the list.
     *
     * @param animations List of animations for this menu
     * @return This builder for use in the builder pattern
     */
    fun setAnimations(animations: MutableList<MenuAnimation>): PagedMenuBuilder {
        this.animations = animations
        return this
    }

    /**
     * Set an item with a specific page, slot, and MenuItem.
     * This is unlike the default overloads for addItem as they set items across all pages.
     * Will expand the menu if the page number provided does not yet exist.
     *
     * @param slot The slot to insert the icon at
     * @param pageNumber The number of the page to insert the icon at
     * @param menuItem The MenuItem to represent this slot
     * @return This builder for use in the builder pattern
     */
    fun setItem(slot: Int, pageNumber: Int, menuItem: MenuItem): PagedMenuBuilder {
        while (pageNumber >= pages.size) addPage()
        pages[pageNumber].setItem(slot, menuItem)
        return this
    }

    /**
     * Set an item with a specific page, slot, and ItemStack.
     * This is unlike the default overloads for addItem as they set items across all pages.
     * Will expand the menu if the page number provided does not yet exist.
     * The ItemStack icon is immediately used to generate a MenuItem without any actions.
     *
     * @param slot The slot to insert the icon at
     * @param pageNumber The number of the page to insert the icon at
     * @param item The ItemStack icon to represent this slot
     * @return This builder for use in the builder pattern
     */
    fun setItem(slot: Int, pageNumber: Int, item: ItemStack): PagedMenuBuilder {
        setItem(slot, pageNumber, MenuItem(item))
        return this
    }

    /**
     * Set an item with a specific, slot, and MenuItem so that is identical across all pages.
     * This will apply to all pages that are created in the future as well.
     * Excluded pages don't need to exist yet, but on creation they will have the item not applied.
     * The ItemStack icon is immediately used to generate a MenuItem without any actions.
     *
     * @param slot The slot to insert the icon at
     * @param item The ItemStack icon to represent this slot
     * @param excludePages Page number vararg of pages that should not have the item added.
     * @return This builder for use in the builder pattern
     */
    fun setStaticItem(slot: Int, item: MenuItem, vararg excludePages: Int = intArrayOf()): PagedMenuBuilder {
        staticItems[slot] = Pair(item, excludePages)
        for ((index, page) in pages.withIndex()) if (!excludePages.contains(index)) page.setItem(slot, item)
        return this
    }

    /**
     * Sets the inventory size for a page with a certain location in the menu.
     * Pages can have different sizes than the default for this menu.
     *
     * @param pageNumber Number of the page to modify
     * @param size New size of the menu, must be between 9 and 54 and be a multiple of 54
     * @return This builder for use in the builder pattern
     */
    fun setPageSize(pageNumber: Int, size: Int): PagedMenuBuilder {
        if (pageNumber !in 0 until pages.size) throw IllegalArgumentException("Invalid page size $pageNumber does not exist!")
        pages[pageNumber].size = size
        return this
    }

    /**
     * Sets the contents of all of the pages of this menu.
     * This will immediately overwrite and delete the current pages in this builder.
     *
     * @param pages Mutable list of the new page items
     * @return This builder for use in the builder pattern
     */
    fun setPages(pages: MutableList<PagedMenu.Page>): PagedMenuBuilder {
        this.pages = pages
        return this
    }

    override fun setInteractionsBlocked(interactionsBlocked: Boolean): PagedMenuBuilder {
        this.interactionsBlocked = interactionsBlocked
        return this
    }

    override fun setOnClose(onClose: (InventoryCloseEvent) -> Unit): PagedMenuBuilder {
        this.onClose = onClose
        return this
    }

    override fun setOnClick(onClick: (InventoryClickEvent) -> Unit): PagedMenuBuilder {
        this.onClick = onClick
        return this
    }

    /**
     * Adds new pages to the menu.
     * Will immediately populate them with the static items that have been set.
     *
     * @param amount Amount of pages to add, defaults to one
     * @param size The size of the pages to add, defaults to default size
     * @return This builder for use in the builder pattern
     */
    @JvmOverloads
    fun addPage(amount: Int = 1, size: Int = defaultSize): PagedMenuBuilder {
        val initialSize = pages.size
        repeat(amount) {
            val pageItems = PagedMenu.Page(defaultTitle.replace(PAGE_NUMBER_PLACEHOLDER, "${pages.size + 1}"), defaultSize)
            for (slot in staticItems.keys) {
                if (!staticItems[slot]!!.second.contains(it + initialSize)) {
                    pageItems.setItem(slot, staticItems[slot]!!.first)
                }
            }
            if (pages.size == 0) this.size = size
            pages.add(pageItems)
        }
        return this
    }

    /**
     * Sets how many pages should be in the menu.
     * If we have too many pages, it deletes off the end.
     * If we have too few pages, it adds new pages using addPage
     *
     * @param length New number of pages in the menu
     * @return This builder for use in the builder pattern
     */
    fun setMenuLength(length: Int): PagedMenuBuilder {
        if (length > pages.size) {
            addPage(amount = length - pages.size)
        } else if (length < pages.size) {
            repeat(pages.size - length) {
                pages.removeLast()
            }
        }
        return this
    }

    /**
     * Sets the page title for one specific page.
     * Supports color codes and the page number placeholder.
     *
     * @param title New title
     * @param pageNumbers Page numbers vararg of pages to apply to
     */
    fun setPageTitle(title: String, vararg pageNumbers: Int) {
        val coloredTitle = ChatColor.translateAlternateColorCodes(colorPrefix, title)
        for (pageNumber in pageNumbers) {
            if (pageNumber >= pages.size) throw IllegalArgumentException("Page number $pageNumber does not yet exist in builder!")
            pages[pageNumber].title = coloredTitle.replace(PAGE_NUMBER_PLACEHOLDER, "${pageNumber + 1}")
        }
    }

    override fun addBorder(borderItem: MenuItem, vararg borders: MenuBuilder.MenuBuilderBorder): PagedMenuBuilder {
        for (borderSide in borders) {
            when (borderSide) {
                // TODO needs updating when dropper menus exist
                MenuBuilder.MenuBuilderBorder.TOP -> for (i in 0 until 9) setStaticItem(i, borderItem)
                MenuBuilder.MenuBuilderBorder.BOTTOM -> for (i in 0 until 9) setStaticItem(size - i - 1, borderItem)
                MenuBuilder.MenuBuilderBorder.LEFT -> for (i in 0 until size step 9) setStaticItem(i, borderItem)
                MenuBuilder.MenuBuilderBorder.RIGHT -> for (i in 0 until size step 9) setStaticItem(i + 8, borderItem)
            }
        }
        return this
    }

    override fun build() = PagedMenu(
        menusAPI,
        pages,
        animations,
        interactionsBlocked = interactionsBlocked,
        onCloseHandler = onClose,
        onClickHandler = onClick
    )

    override fun clone(): PagedMenuBuilder {
        val newPages = ArrayList<PagedMenu.Page>()
        for (page in pages) {
            val pageItems = HashMap<Int, MenuItem>()
            for ((slot, item) in page.items) pageItems[slot] = item
            newPages.add(PagedMenu.Page(page.title, pageItems, page.size))
        }
        return PagedMenuBuilder(menusAPI, defaultTitle, defaultSize, colorPrefix = colorPrefix)
            .setPages(newPages)
            .setAnimations(animations)
            .setInteractionsBlocked(interactionsBlocked)
            .setOnClose(onClose)
            .setOnClick(onClick)
    }

}