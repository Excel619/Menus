package com.gmail.excel8392.menus

import com.gmail.excel8392.menus.builder.BasicMenuBuilder
import com.gmail.excel8392.menus.builder.MenuBuilder
import com.gmail.excel8392.menus.builder.PagedMenuBuilder
import com.gmail.excel8392.menus.menu.Menu
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

interface MenusAPI {

    val plugin: Plugin

    fun openMenu(player: Player, menu: Menu)

    fun openMenu(player: Player, menu: MenuBuilder<*>) = openMenu(player, menu.build())

    fun openMenu(player: Player, menu: MenuType) = openMenu(player, menu.generateMenu(player))

    fun basicMenuBuilder(title: String, size: Int, colorPrefix: Char = '&') = BasicMenuBuilder(this, title, size, colorPrefix)

    // Interface @JvmOverloads don't exist so this is to keep it compatible with JVM
    fun basicMenuBuilder(title: String, size: Int) = BasicMenuBuilder(this, title, size)

    fun pagedMenuBuilder(title: String, defaultSize: Int, colorPrefix: Char = '&') = PagedMenuBuilder(this, title, defaultSize, colorPrefix)

    // Interface @JvmOverloads don't exist so this is to keep it compatible with JVM
    fun pagedMenuBuilder(title: String, defaultSize: Int) = PagedMenuBuilder(this, title, defaultSize)

}