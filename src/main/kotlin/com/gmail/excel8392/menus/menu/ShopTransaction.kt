package com.gmail.excel8392.menus.menu

import com.gmail.excel8392.menus.menu.Menu
import org.bukkit.entity.Player

interface ShopTransaction {

    fun applyTransaction(viewer: Player, menu: Menu): Boolean

}