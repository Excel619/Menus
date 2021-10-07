package com.gmail.excel8392.menus.util

import com.gmail.excel8392.menus.menu.MenuItem
import com.gmail.excel8392.menus.menu.PagedMenu
import kotlin.math.ceil
import kotlin.math.max

/**
 * Utility methods for Menu related operations in this library.
 */
internal object MenuUtil {

    /**
     * Returns the minimum required amount of inventory slots for a collection of slot numbers.
     * This value will be between 9 and 54, and will be a multiple of 9.
     *
     * @param slots Set of slot numbers, obviously can't contain duplicates
     */
    internal fun getMinSlots(slots: Set<Int>): Int {
        val lastSlot = slots.stream().mapToInt{it}.max().orElse(9)
        if (slots.size > 54 || lastSlot >= 54) throw IllegalArgumentException("Inventory slots must be between 0 and 53!")
        val minSlots = ceil(lastSlot / 9.0).toInt() * 9
        return max(minSlots, 9)
    }

    /**
     * Applies the Menu's defaultInteractionsBlocked to all MenuItems that haven't had their interactionsBlocked set.
     *
     * Interactions blocked determines if a player can move an item in a menu, or if it will be forced to remain static.
     * Each menu item can have it set manually, but if none set the the menu's default is applied.
     */
    internal fun applyDefaultInteractionsBlocked(items: Map<Int, MenuItem>, interactionsBlocked: Boolean) {
        for ((_, item) in items) item.applyDefaultInteractionsBlocked(interactionsBlocked)
    }

    /**
     * Gets the first empty page which has at least one slot empty in it.
     * This uses the MenuPageItems's size to determine if it is full,
     * it will not increase a page's size to fit more items.
     * This method will add a new page to the list of pages if every page is full.
     *
     * @return First page with at least one empty slot
     */
    internal fun getFirstEmptyPage(pages: MutableList<PagedMenu.PageItems>, newPageDefaultSize: Int): PagedMenu.PageItems {
        for (page in pages) if (!page.isFull()) return page
        return PagedMenu.PageItems(newPageDefaultSize).also { pages.add(it) }
    }

    /**
     * Gets the first slot which is empty in a map of slots and menu items.
     *
     * @throws IllegalArgumentException if the menu items are full
     */
    internal fun getFirstEmptySlot(items: Map<Int, MenuItem>, menuSize: Int): Int {
        for (slot in 0 until menuSize) if (!items.containsKey(slot)) return slot
        throw IllegalArgumentException("Menu items are full, cannot getFirstEmptySlot()!")
    }

    /**
     * Checks if a given menu size is valid, meaning it is greater than 0,
     * less than or equal to 54, and is a multiple of 9.
     *
     * @return Whether or not this size is valid for bukkit menus
     */
    internal fun isValidMenuSize(size: Int) = size in 1..54 && size % 9 == 0

}