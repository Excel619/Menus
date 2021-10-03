package com.gmail.excel8392.menus.menu

import com.gmail.excel8392.menus.util.MenuUtil

class MenuPageItems(items: MutableMap<Int, MenuItem>, size: Int) {

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