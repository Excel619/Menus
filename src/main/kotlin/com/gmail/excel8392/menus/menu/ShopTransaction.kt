package com.gmail.excel8392.menus.menu

import org.bukkit.entity.Player

/**
 * Transaction that is executed for purchases in a shop menu.
 */
interface ShopTransaction {

    /**
     * Execute this transaction.
     *
     * @param viewer Player clicking on the menu item
     * @param menu Shop menu
     * @return Success
     */
    fun applyTransaction(viewer: Player, menu: Menu): Boolean

}