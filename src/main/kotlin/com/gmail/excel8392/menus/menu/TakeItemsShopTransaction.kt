package com.gmail.excel8392.menus.menu

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Special type of transaction which takes different amounts of ItemStacks from the viewer.
 * Items taken are checked using the isSimilar check for ItemStacks
 * Upon failure (missing items) returns false, which will throw a Runtime exception if using ShopMenuItem.
 *
 * @constructor Create a take items shop transaction
 *
 * @property items Items to take vararg, pair key is item, pair value is amount
 *
 * @see com.gmail.excel8392.menus.menu.ShopTransaction
 * @see com.gmail.excel8392.menus.menu.ShopCondition
 * @see com.gmail.excel8392.menus.menu.ShopMenuItem
 */
class TakeItemsShopTransaction constructor(
    vararg val items: Pair<ItemStack, Int>
): ShopTransaction {

    override fun applyTransaction(viewer: Player, menu: Menu): Boolean {
        for (pair in items) {
            val item = pair.first
            val amount = pair.second
            var leftToRemove = amount
            for ((slot, target) in viewer.inventory.contents.withIndex()) {
                if (target.isSimilar(item)) {
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
            if (leftToRemove > 0) {
                var leftToAdd = amount - leftToRemove
                while (leftToAdd > 0) {
                    viewer.inventory.addItem(item.clone().also {
                        it.amount = leftToAdd % item.maxStackSize
                        leftToAdd -= it.amount
                    })
                }
                return false
            }
        }
        viewer.updateInventory() // TODO test if viewer.updateInventory is needed
        return true
    }

}