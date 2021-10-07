package com.gmail.excel8392.menus.menu

import com.gmail.excel8392.menus.menu.Menu
import org.bukkit.entity.Player

interface ShopCondition {

    fun checkCondition(player: Player, menu: Menu): Boolean

}