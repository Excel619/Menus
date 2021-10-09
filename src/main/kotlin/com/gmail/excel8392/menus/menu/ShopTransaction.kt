package com.gmail.excel8392.menus.menu

import org.bukkit.entity.Player

interface ShopTransaction {

    fun applyTransaction(viewer: Player, menu: Menu): Boolean

}