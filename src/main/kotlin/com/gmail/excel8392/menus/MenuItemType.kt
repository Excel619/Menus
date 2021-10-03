package com.gmail.excel8392.menus

import com.gmail.excel8392.menus.menu.MenuItem
import org.bukkit.entity.Player

interface MenuItemType {

    fun generateMenuItem(player: Player): MenuItem

}