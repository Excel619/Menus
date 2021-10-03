package com.gmail.excel8392.menus.action

import com.gmail.excel8392.menus.menu.Menu
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * {@inheritDoc}
 *
 * Action for MenuItems that executes a Supplier<Menu> and opens it upon execute.
 *
 * @param menuSupplier Lambda supplier for the menu to open
 */
class OpenMenuAction(private val menuSupplier: () -> Menu): Action {

    /**
     * {@inheritDoc}
     *
     * @param menu Menu to open
     */
    constructor(menu: Menu) : this({menu})

    /** {@inheritDoc} */
    override fun execute(event: InventoryClickEvent) {
        event.whoClicked.openInventory(menuSupplier().inventory)
    }

}