package com.gmail.excel8392.menus.menu

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Shop transaction that grants items to the player upon purchase.
 *
 * @constructor Create Grant items shop transaction
 *
 * @property items Items to give to the player
 * @property dropOnFail If the items should be dropped on the group if inventory is full
 */
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