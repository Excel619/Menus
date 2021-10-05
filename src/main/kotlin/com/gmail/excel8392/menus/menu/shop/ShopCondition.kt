package com.gmail.excel8392.menus.menu.shop

import org.bukkit.entity.Player

interface ShopCondition {

    fun checkCondition(player: Player): Boolean

}