package com.gmail.excel8392

import com.gmail.excel8392.menus.MenuGenerator
import com.gmail.excel8392.menus.builder.BasicMenuBuilder
import com.gmail.excel8392.menus.builder.MenuItemBuilder
import com.gmail.excel8392.menus.menu.Menu
import org.bukkit.Material
import org.bukkit.entity.Player

enum class MyMenuGenerator(val menuBuilder: BasicMenuBuilder): MenuGenerator {

    PLAYER_STATISTICS(
        BasicMenuBuilder(TODO(), 27)
            .setItem(4, MenuItemBuilder(Material.OAK_SIGN)
                .setName("Stats")
                .build())) {
        override fun generateMenu(player: Player): Menu {
            // Apply items that would display specifically for this player
            menuBuilder.title = "${player.name}'s Stats"
            return menuBuilder.build()
        }
    };

}