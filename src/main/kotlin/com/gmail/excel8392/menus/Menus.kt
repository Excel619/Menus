package com.gmail.excel8392.menus

import com.gmail.excel8392.menus.menu.Menu
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class Menus(val plugin: Plugin): MenusAPI {

    companion object {
        private val menusAPIs = HashMap<Plugin, Menus>()
    }

    init {
        if (menusAPIs.containsKey(plugin)) throw IllegalArgumentException("A MenusAPI has already been created for plugin \"${plugin.name}\"!")
        menusAPIs[plugin] = this
        Bukkit.getPluginManager().registerEvents(MenusListener(plugin), plugin)
    }

    override fun openMenu(player: Player, menu: Menu) {
        player.openInventory(menu.inventory)
    }


}