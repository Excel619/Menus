package com.gmail.excel8392.menus.menu.shop

import org.bukkit.Material
import org.bukkit.entity.Player

class ShopConditionItem(val material: Material, val amount: Int): ShopCondition {

    override fun checkCondition(player: Player) = player.inventory.contains(material, amount)

}