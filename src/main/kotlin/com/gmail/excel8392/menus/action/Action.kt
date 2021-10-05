package com.gmail.excel8392.menus.action

import org.bukkit.event.inventory.InventoryClickEvent

/**
 * Action performed on the press of a MenuItem icon in a Menu.
 * Actions can be performed multiple times with different events.
 *
 * @see com.gmail.excel8392.menus.menu.MenuItem
 */
interface Action {

    /**
     * Handles this action executing upon the click of a MenuItem icon in a Menu.
     * Any changes made to the InventoryClickEvent parameter will be applied to the bukkit event.
     * @param event - The bukkit event that is calling for the execute of this action
     */
    fun execute(event: InventoryClickEvent)

}