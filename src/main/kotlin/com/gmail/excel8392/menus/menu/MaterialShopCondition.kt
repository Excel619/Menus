package com.gmail.excel8392.menus.menu

import org.bukkit.Material
import org.bukkit.entity.Player

class MaterialShopCondition(val material: Material, val amount: Int): ShopCondition {

    override fun checkCondition(player: Player, menu: Menu) = player.inventory.contains(material, amount)

}