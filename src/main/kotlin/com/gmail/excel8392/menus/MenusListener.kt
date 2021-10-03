package com.gmail.excel8392.menus

import com.gmail.excel8392.menus.menu.Menu
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.plugin.Plugin

class MenusListener(val plugin: Plugin): Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    fun onInventoryClick(event: InventoryClickEvent) {
        // Check that the inventory holder is a custom Menu
        if (event.inventory.holder !is Menu) return

        // Cast to custom Menu
        val menu = event.inventory.holder as Menu
        // Ensure that this menu is to be handled by the plugin that registered this Menus API.
        // This is significant because multiple Menus APIs can be created by different plugins,
        // and without this check each menu click would be handled several times.
        if (menu.plugin != plugin) return
        // Ensure that the slot clicked is not empty
        if (event.currentItem?.type == Material.AIR) return

        // Fire the on click event for the menu
        menu.onClick(event)

        // Get the MenuItem clicked
        val clickedMenuItem = menu.items[event.slot] ?: return
        // If interactions for the MenuItem are blocked, cancel the event
        event.isCancelled = clickedMenuItem.interactionsBlocked

        // Fire the on click event for the menu item
        clickedMenuItem.onClick(event)
    }

    @EventHandler(priority = EventPriority.NORMAL)
    fun onInventoryClose(event: InventoryCloseEvent) {
        // Check that the inventory holder is a custom Menu
        if (event.inventory.holder !is Menu) return

        // Cast to custom Menu
        val menu = event.inventory.holder as Menu
        // Ensure that this menu is to be handled by the plugin that registered this Menus API.
        // This is significant because multiple Menus APIs can be created by different plugins,
        // and without this check each menu click would be handled several times.
        if (menu.plugin != plugin) return

        // Fire the on close event for the menu
        menu.onClose(event)
    }

}