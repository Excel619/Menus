package com.gmail.excel8392.menus.animation

import com.gmail.excel8392.menus.menu.Menu
import org.bukkit.entity.Player

interface MenuAnimation {

    fun getInterval(): Int

    fun tickAnimation(viewer: Player, menu: Menu)

}