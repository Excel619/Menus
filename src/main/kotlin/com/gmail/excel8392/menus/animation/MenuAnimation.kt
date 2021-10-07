package com.gmail.excel8392.menus.animation

import com.gmail.excel8392.menus.menu.Menu
import org.bukkit.entity.Player

/**
 * MenuAnimations are wrappers for a bukkit task whose purpose is to update the contents of the inventory every interval.
 * The bukkit repeating sync task is started when a player opens the menu and stopped when the player closes it.
 * For every open menu (even of the same instance) a new bukkit task will be created for each player.
 *
 * @see com.gmail.excel8392.menus.menu.Menu
 */
interface MenuAnimation {

    /** The interval for which to wait before ticking this animation again, in ticks */
    val interval: Int

    /**
     * Update this animation with the provided viewer and menu.
     * This is run synchronously, and it is advised against scheduling asynchronous tasks in this method.
     *
     * @param viewer The player viewing this menu
     * @param menu The menu the player is viewing
     */
    fun tickAnimation(viewer: Player, menu: Menu)

}