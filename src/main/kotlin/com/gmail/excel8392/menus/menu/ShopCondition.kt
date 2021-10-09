package com.gmail.excel8392.menus.menu

import org.bukkit.entity.Player

interface ShopCondition {

    fun checkCondition(player: Player, menu: Menu): Boolean

}