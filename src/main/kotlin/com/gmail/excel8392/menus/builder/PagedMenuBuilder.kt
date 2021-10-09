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

class PagedMenuBuilder @JvmOverloads constructor(
    var menusAPI: MenusAPI,
    var title: String,
    val defaultSize: Int,
    private var colorPrefix: Char = '&'
): MenuBuilder<PagedMenuBuilder> {

    override var size = defaultSize

    var pages: MutableList<PagedMenu.PageItems> = ArrayList()
        private set
    private var staticItems = HashMap<Int, Pair<MenuItem, IntArray>>()
    private var animations: MutableList<MenuAnimation> = LinkedList<MenuAnimation>()
    private var interactionsBlocked = true
    private var onClose: (InventoryCloseEvent) -> Unit = {}
    private var onClick: (InventoryClickEvent) -> Unit = {}

    @JvmOverloads
    constructor(menusAPI: MenusAPI, defaultSize: Int, colorPrefix: Char = '&'): this(menusAPI, "", defaultSize, colorPrefix = colorPrefix)

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

    fun setAnimations(animations: MutableList<MenuAnimation>): PagedMenuBuilder {
        this.animations = animations
        return this
    }

    fun setItem(slot: Int, pageNumber: Int, menuItem: MenuItem): PagedMenuBuilder {
        while (pageNumber >= pages.size) addPage()
        pages[pageNumber].setItem(slot, menuItem)
        return this
    }

    fun setItem(slot: Int, pageNumber: Int, item: ItemStack): PagedMenuBuilder {
        setItem(slot, pageNumber, MenuItem(item))
        return this
    }

    fun setStaticItem(slot: Int, item: MenuItem, vararg excludePages: Int = intArrayOf()): PagedMenuBuilder {
        staticItems[slot] = Pair(item, excludePages)
        for ((index, page) in pages.withIndex()) if (!excludePages.contains(index)) page.setItem(slot, item)
        return this
    }

    fun setPageSize(pageNumber: Int, size: Int): PagedMenuBuilder {
        if (pageNumber !in 0 until pages.size) throw IllegalArgumentException("Invalid page size $pageNumber does not exist!")
        pages[pageNumber].size = size
        return this
    }

    fun setPages(pages: MutableList<PagedMenu.PageItems>): PagedMenuBuilder {
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

    @JvmOverloads
    fun addPage(amount: Int = 1, size: Int = defaultSize) {
        val initialSize = pages.size
        repeat(amount) {
            val pageItems = PagedMenu.PageItems(defaultSize)
            for (slot in staticItems.keys) {
                if (!staticItems[slot]!!.second.contains(it + initialSize)) {
                    pageItems.setItem(slot, staticItems[slot]!!.first)
                }
            }
            if (pages.size == 0) this.size = size
            pages.add(pageItems)
        }
    }

    fun setMenuLength(length: Int) {
        if (length > pages.size) {
            addPage(amount = length - pages.size)
        } else if (length < pages.size) {
            repeat(pages.size - length) {
                pages.removeLast()
            }
        }
    }

    override fun addBorder(borderItem: ItemStack, vararg borders: MenuBuilder.MenuBuilderBorder): PagedMenuBuilder {
        // TODO change to work on all pages
        super.addBorder(borderItem, *borders)
        return this
    }

    override fun build() = PagedMenu(
        menusAPI,
        ChatColor.translateAlternateColorCodes(colorPrefix, title),
        pages,
        animations,
        interactionsBlocked = interactionsBlocked,
        onClose = onClose,
        onClick = onClick
    )

    override fun clone(): PagedMenuBuilder {
        val newPages = ArrayList<PagedMenu.PageItems>()
        for (page in pages) {
            val pageItems = HashMap<Int, MenuItem>()
            for ((slot, item) in page.items) pageItems[slot] = item
            newPages.add(PagedMenu.PageItems(pageItems, page.size))
        }
        return PagedMenuBuilder(menusAPI, title, defaultSize, colorPrefix = colorPrefix)
            .setPages(newPages)
            .setAnimations(animations)
            .setInteractionsBlocked(interactionsBlocked)
            .setOnClose(onClose)
            .setOnClick(onClick)
    }

}