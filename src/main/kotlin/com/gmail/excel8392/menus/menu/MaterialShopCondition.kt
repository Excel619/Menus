package com.gmail.excel8392.menus.menu

import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * Shop condition that checks if the player has items totalling to a number of a specific material.
 *
 * @constructor Create Material shop condition
 *
 * @property material Material to check
 * @property amount Amount of items required
 *
 * @see com.gmail.excel8392.menus.menu.ShopCondition
 * @see com.gmail.excel8392.menus.menu.ShopTransaction
 * @see com.gmail.excel8392.menus.menu.ShopMenuItem
 */
class MaterialShopCondition(val material: Material, val amount: Int): ShopCondition {

    override fun checkCondition(viewer: Player, menu: Menu) = viewer.inventory.contains(material, amount)

}