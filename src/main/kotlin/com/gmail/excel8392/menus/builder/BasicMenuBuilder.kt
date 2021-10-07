package com.gmail.excel8392.menus.builder

import com.gmail.excel8392.menus.animation.MenuAnimation
import com.gmail.excel8392.menus.menu.Menu
import com.gmail.excel8392.menus.menu.MenuItem
import com.gmail.excel8392.menus.util.MenuUtil
import org.bukkit.ChatColor
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.plugin.Plugin
import java.util.LinkedList

class BasicMenuBuilder @JvmOverloads constructor(
    var plugin: Plugin,
    var title: String,
    override var size: Int,
    private var colorPrefix: Char = '&'
): MenuBuilder {

    // TODO override default interface methods to return this class type

    private var items: MutableMap<Int, MenuItem> = HashMap()
    private var animations: MutableList<MenuAnimation> = LinkedList<MenuAnimation>()
    private var interactionsBlocked = true
    private var onClose: (InventoryCloseEvent) -> Unit = {}
    private var onClick: (InventoryClickEvent) -> Unit = {}

    init {
        if (!MenuUtil.isValidMenuSize(size)) throw IllegalArgumentException("$size is not a valid bukkit menu size!")
    }

    @JvmOverloads
    constructor(plugin: Plugin, size: Int, colorPrefix: Char = '&'): this(plugin, "", size, colorPrefix = colorPrefix)

    override fun setItem(slot: Int, menuItem: MenuItem): BasicMenuBuilder {
        if (slot !in 0 until size) throw IllegalArgumentException("Cannot insert MenuItem at slot $slot because it is greater than the menu size of $size")
        items[slot] = menuItem
        return this
    }

    override fun addItem(menuItem: MenuItem): BasicMenuBuilder {
        items[MenuUtil.getFirstEmptySlot(items, size)] = menuItem
        return this
    }

    override fun addAnimation(menuAnimation: MenuAnimation): BasicMenuBuilder {
        animations.add(menuAnimation)
        return this
    }

    fun setAnimations(animations: MutableList<MenuAnimation>): BasicMenuBuilder {
        this.animations = animations
        return this
    }

    fun setItems(items: MutableMap<Int, MenuItem>): BasicMenuBuilder {
        if (items.size > size)
        this.items = items
        return this
    }

    override fun setInteractionsBlocked(interactionsBlocked: Boolean): BasicMenuBuilder {
        this.interactionsBlocked = interactionsBlocked
        return this
    }

    override fun setOnClose(onClose: (InventoryCloseEvent) -> Unit): BasicMenuBuilder {
        this.onClose = onClose
        return this
    }

    override fun setOnClick(onClick: (InventoryClickEvent) -> Unit): BasicMenuBuilder {
        this.onClick = onClick
        return this
    }

    override fun build() = Menu(
        plugin,
        ChatColor.translateAlternateColorCodes(colorPrefix, title),
        items,
        size,
        animations,
        interactionsBlocked = interactionsBlocked,
        onCloseHandler = onClose,
        onClickHandler = onClick
    )

    override fun clone(): BasicMenuBuilder {
        val newItems = HashMap<Int, MenuItem>()
        for ((slot, menuItem) in items) newItems[slot] = menuItem
        return BasicMenuBuilder(plugin, title, size, colorPrefix = colorPrefix)
            .setItems(newItems)
            .setAnimations(animations)
            .setInteractionsBlocked(interactionsBlocked)
            .setOnClose(onClose)
            .setOnClick(onClick)
    }

}