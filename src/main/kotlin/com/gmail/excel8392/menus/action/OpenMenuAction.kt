package com.gmail.excel8392.menus.action

import com.gmail.excel8392.menus.MenuType
import com.gmail.excel8392.menus.menu.Menu
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * {@inheritDoc}
 *
 * Action for MenuItems that executes a Supplier<Menu> and opens it upon execute.
 * Menu can be customized for a player using the InventoryClickEvent parameter in the lambda.
 *
 * @param menuSupplier Lambda supplier for the menu to open
 */
class OpenMenuAction(private val menuSupplier: (InventoryClickEvent) -> Menu): Action {

    /**
     * {@inheritDoc}
     *
     * @param menu Menu to open
     */
    constructor(menu: Menu) : this({menu})

    /**
     * TODO
     */
    constructor(menu: MenuType) : this({menu.generateMenu(it.whoClicked as Player)})

    /** {@inheritDoc} */
    override fun execute(event: InventoryClickEvent) {
        if (event.whoClicked !is Player) return
        event.whoClicked.openInventory(menuSupplier(event).inventory)
    }

}