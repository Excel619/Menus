package com.gmail.excel8392.menus.menu

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

interface MenuInventoryHolder: InventoryHolder {

    val inventory: Inventory
        override get()

    override fun getInventory(): Inventory {
        TODO("Not yet implemented")
    }
}