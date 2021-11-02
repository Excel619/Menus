package com.gmail.excel8392.menus.menu

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Shop condition that checks if the player has a number of different types of items.
 *
 * @constructor Create Item shop condition
 *
 * @property items Pair of item and amount vararg, checks that player has all
 *
 * @see com.gmail.excel8392.menus.menu.ShopCondition
 * @see com.gmail.excel8392.menus.menu.ShopTransaction
 * @see com.gmail.excel8392.menus.menu.ShopMenuItem
 */
class ItemShopCondition constructor(
    vararg val items: Pair<ItemStack, Int>
): ShopCondition {

    override fun checkCondition(viewer: Player, menu: Menu): Boolean {
        for (pair in items) {
            var contains = 0
            itemCheck@ for (target in viewer.inventory.contents) {
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