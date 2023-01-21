package com.gmail.excel8392.menus

import com.gmail.excel8392.menus.handle.MenusListener
import com.gmail.excel8392.menus.menu.Menu
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/**
 * Menus Library
 *
 * To create an API instance, create an instance of this class.
 * Each project depending on this library is recommended to have a separate instance of this class.
 * Menus instances are tied to a bukkit plugin, and register their listeners and timers using it.
 *
 * Use BasicMenuBuilder, PagedMenuBuilder, MenuItemBuilder to create menus.
 * To display a menu to a player, use Menu#openPlayer(Player).
 *
 * @constructor Create a Menus reference bound to a plugin
 *
 * @property plugin The plugin registering this instance of the API
 */
class Menus(override val plugin: Plugin): MenusAPI {

    companion object {
        private val menusAPIs = HashMap<Plugin, Menus>()
    }

    init {
        if (menusAPIs.containsKey(plugin)) throw IllegalArgumentException("A MenusAPI has already been created for plugin \"${plugin.name}\"!")
        menusAPIs[plugin] = this
        Bukkit.getPluginManager().registerEvents(MenusListener(this), plugin)
    }

    override fun openMenu(player: Player, menu: Menu) = menu.openMenu(player)


}