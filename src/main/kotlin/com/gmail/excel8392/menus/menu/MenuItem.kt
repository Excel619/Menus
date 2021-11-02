package com.gmail.excel8392.menus.menu

import com.gmail.excel8392.menus.action.MenuAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.LinkedList
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * An ItemStack wrapper for items inside custom menus.
 * Contains a list of actions to execute on click, and whether or not to allow players to move this icon freely (defaults to menu default).
 *
 * @constructor Create a MenuItem with no actions
 *
 * @property icon ItemStack icon representing this MenuItem
 * @param interactionsBlocked If specified (not null) overrides the menu default interactions blocked.
 */
open class MenuItem @JvmOverloads constructor(
    val icon: ItemStack,
    interactionsBlocked: Boolean? = null
) {

    private var internalInteractionsBlocked: Boolean? = interactionsBlocked

    /**
     * Whether or not to block interactions for this MenuItem.
     * This uses an anonymous delegate to ensure that it defaults to the menu default value.
     */
    var interactionsBlocked: Boolean by object: ReadWriteProperty<Any?, Boolean> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean =
            internalInteractionsBlocked ?: throw IllegalStateException("MenuItem#interactionsBlocked not overridden by MenuItem holder!")
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) { internalInteractionsBlocked = value }
    }

    val actions = LinkedList<MenuAction>()

    fun addAction(action: MenuAction) {
        actions.add(action)
    }

    fun onClick(event: InventoryClickEvent, menu: Menu) {
        for (action in actions) action.execute(event, menu)
    }

    fun applyDefaultInteractionsBlocked(defaultInteractionsBlocked: Boolean) = internalInteractionsBlocked ?: run { interactionsBlocked = defaultInteractionsBlocked }

}