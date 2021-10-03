package com.gmail.excel8392.menus.menu

import com.gmail.excel8392.menus.util.MenuUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.plugin.Plugin

open class Menu @JvmOverloads constructor(
    val plugin: Plugin,
    var title: String,
    val items: Map<Int, MenuItem>,
    var size: Int = 0,
    interactionsBlocked: Boolean = true,
    val onClick: (InventoryClickEvent) -> Unit = {},
    val onClose: (InventoryCloseEvent) -> Unit = {}
): InventoryHolder {

    // It should be noted that we are leaking "this" in the Bukkit.createInventory,
    // but this is of no concern because it does not use the InventoryHolder
    private var inventory: Inventory =
        if (title.isEmpty() || title.isBlank()) Bukkit.createInventory(this, size)
        else Bukkit.createInventory(this, size, title)


    init {
        // Set size to a valid inventory size (between 0-54, multiple of 9)
        if (size !in 1..54 || size % 9 != 0) size = MenuUtil.getMinSlots(items.keys)
        if (items.size > 54 || items.size > size) throw IllegalArgumentException("Menu cannot have more items than determined GUI size!")
        fillInventory() // Check warning for fillInventory
        // For all of the menu items in the inventory that don't have interactionsBlocked set, apply this menu's default
        MenuUtil.applyDefaultInteractionsBlocked(items, interactionsBlocked)
    }

    /**
     * WARNING: Do not use this to open a menu, instead use Menu#openMenu!
     *
     * TODO
     */
    override fun getInventory(): Inventory = inventory

    open fun openMenu(player: Player) {
        player.openInventory(inventory)
    }

    /**
     * Fills up Menu#inventory with items defined in constructor.
     *
     * <b>This is a dangerous function to overwrite!</b>
     * This is called in the init block of this class, so when overridden it
     * will be called when the derived class <b>has not yet been constructed!</b>
     * Overwritten function should not include any references to non-constructor instance data or
     * require the execution of the init block first.
     */
    protected open fun fillInventory() {
        for ((slot, item) in items) inventory.setItem(slot, item.icon)
    }

}