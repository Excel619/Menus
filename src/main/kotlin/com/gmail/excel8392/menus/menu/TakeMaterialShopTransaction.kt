package com.gmail.excel8392.menus.menu

import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * Special type of transaction which takes different amounts of items of a certain Material from the viewer.
 * Upon failure (missing items/materials) returns false, which will throw a Runtime exception if using ShopMenuItem.
 *
 * @constructor Create a take material shop transaction
 *
 * @property material Material to take from
 * @property amount Amount of the material to take
 *
 * @see com.gmail.excel8392.menus.menu.ShopTransaction
 * @see com.gmail.excel8392.menus.menu.ShopCondition
 * @see com.gmail.excel8392.menus.menu.ShopMenuItem
 */
class TakeMaterialShopTransaction @JvmOverloads constructor(
    val material: Material,
    val amount: Int = 1
): ShopTransaction {

    override fun applyTransaction(viewer: Player, menu: Menu): Boolean {
        if (!MaterialShopCondition(material, amount).checkCondition(viewer, menu)) return false
        var leftToRemove = amount
        for ((slot, target) in viewer.inventory.contents.withIndex()) {
            if (target.type == material) {
                if (target.amount > leftToRemove) {
                    target.amount -= leftToRemove
                    break
                } else {
                    viewer.inventory.setItem(slot, null)
                    if (target.amount < leftToRemove) {
                        leftToRemove -= target.amount
                    } else break
                }
            }
        }
        viewer.updateInventory() // TODO
        if (leftToRemove > 0) throw IllegalStateException("Internal error occurred in TakeMaterialShopTransaction, failed to remove materials!")
        return true
    }

}