package com.gmail.excel8392.menus.menu

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ItemShopCondition constructor(
    vararg val items: Pair<ItemStack, Int>
): ShopCondition {

    override fun checkCondition(player: Player, menu: Menu): Boolean {
        for (pair in items) {
            var contains = 0
            itemCheck@ for (target in player.inventory.contents) {
                if (target.isSimilar(pair.first)) {
                    contains += target.amount
                    if (contains >= pair.second) break@itemCheck
                }
            }
            if (contains > 0) return false
        }
        return true
    }

}