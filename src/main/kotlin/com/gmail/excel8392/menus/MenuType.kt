package com.gmail.excel8392.menus

import com.gmail.excel8392.menus.menu.Menu
import org.bukkit.entity.Player

interface MenuType {

    fun generateMenu(player: Player): Menu

}