package com.gmail.excel8392.menus

import com.gmail.excel8392.menus.builder.MenuBuilder
import com.gmail.excel8392.menus.menu.Menu
import org.bukkit.entity.Player

interface MenusAPI {

    fun openMenu(player: Player, menu: Menu)

    fun openMenu(player: Player, menu: MenuBuilder) = openMenu(player, menu.build())

    fun openMenu(player: Player, menu: MenuType) = openMenu(player, menu.generateMenu(player))

}