package com.gmail.excel8392.menus.menu

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.plugin.Plugin

class ShopMenu @JvmOverloads constructor(
    plugin: Plugin,
    title: String,
    items: Map<Int, MenuItem>,
    size: Int = 0,
    interactionsBlocked: Boolean = true,
    onClick: (InventoryClickEvent) -> Unit = {},
    onClose: (InventoryCloseEvent) -> Unit = {}
): Menu(
    plugin,
    title,
    items,
    size,
    interactionsBlocked,
    onClick,
    onClose
) {

}