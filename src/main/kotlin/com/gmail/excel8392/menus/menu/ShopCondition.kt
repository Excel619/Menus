package com.gmail.excel8392.menus.menu

import org.bukkit.entity.Player

/**
 * A condition that is checked when any player is attempting to purchase items from a shop menu.
 */
interface ShopCondition {

    /**
     * Execute logic for this condition and return whether or not the player should be allowed to proceed with this transaction.
     *
     * @param viewer Player clicking on the menu item
     * @param menu Shop menu
     * @return Condition success or not
     */
    fun checkCondition(viewer: Player, menu: Menu): Boolean

}