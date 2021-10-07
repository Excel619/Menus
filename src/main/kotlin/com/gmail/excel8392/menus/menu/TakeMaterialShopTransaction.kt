package com.gmail.excel8392.menus.menu

import org.bukkit.Material
import org.bukkit.entity.Player

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