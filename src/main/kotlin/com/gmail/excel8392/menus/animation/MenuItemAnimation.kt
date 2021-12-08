package com.gmail.excel8392.menus.animation

import com.gmail.excel8392.menus.menu.Menu
import org.bukkit.entity.Player

/**
 * Coming soon!
 *
 * This class would allow custom animations for the item name, lore, material, count, durability, and other meta.
 */
class MenuItemAnimation(override val interval: Int): MenuAnimation {

    override fun tickAnimation(viewer: Player, menu: Menu) {
        throw IllegalStateException("Menu item animations are coming soon!")
    }

}