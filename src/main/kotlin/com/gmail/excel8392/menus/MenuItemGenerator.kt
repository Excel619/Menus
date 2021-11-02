package com.gmail.excel8392.menus

import com.gmail.excel8392.menus.menu.MenuItem
import org.bukkit.entity.Player

/**
 * Interface used to be implemented by <b>an enum</b>.
 * All values of the enum will be MenuItem generators.
 *
 * This is intended to be used if you wish you create multiple similar MenuItems for different Menus that vary based on player.
 * By using a MenuItemGenerator, you can keep initialization of the static elements of the MenuItem by doing it in the enum constructor,
 * and adding the dynamic elements on generation.
 *
 * @see com.gmail.excel8392.menus.menu.MenuItem
 */
interface MenuItemGenerator {

    /**
     * Generate the MenuItem using the viewer.
     * This should only add dynamic elements that vary according to player, and not modify or create static elements.
     *
     * @param player MenuItem viewer
     * @return Generated MenuItem
     */
    fun generateMenuItem(player: Player): MenuItem

}