package com.gmail.excel8392.menus.menu

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GrantItemsShopTransaction @JvmOverloads constructor(
    vararg val items: ItemStack,
    val dropOnFail: Boolean = true
): ShopTransaction {

    override fun applyTransaction(viewer: Player, menu: Menu): Boolean {
        for (item in items) {
            val leftOver = viewer.inventory.addItem(item)
            viewer.updateInventory() // TODO
            if (leftOver.isNotEmpty()) {
                if (dropOnFail) {
                    for (itemToDrop in leftOver.values) viewer.world.dropItem(viewer.location, itemToDrop)
                } else return false
            }
        }
        return true
    }

}