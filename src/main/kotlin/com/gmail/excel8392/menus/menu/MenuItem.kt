package com.gmail.excel8392.menus.menu

import com.gmail.excel8392.menus.action.MenuAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.LinkedList

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

    /** Nullable value for interactions blocked, null signifying menu default (overwrite) */
    private var internalInteractionsBlocked: Boolean? = interactionsBlocked

    /**
     * Whether or not to block interactions for this MenuItem. Wrapper of the internal value which is nullable.
     * This uses custom accessors so that it is exposed as a non null value.
     */
    var interactionsBlocked: Boolean
        get() = internalInteractionsBlocked ?: throw IllegalStateException("MenuItem#interactionsBlocked not overridden by MenuItem holder!")
        set(value) { internalInteractionsBlocked = value }

    /** List of actions to execute on click */
    val actions = LinkedList<MenuAction>()

    /**
     * Add an action to execute on click. Will execute in order after the actions added previously.
     *
     * @param action New action to add
     */
    fun addAction(action: MenuAction) {
        actions.add(action)
    }

    /**
     * Execute the logic on click for this menu item.
     *
     * @param event Bukkit click event wrapper
     * @param menu Menu that this button is in
     */
    fun onClick(event: InventoryClickEvent, menu: Menu) {
        for (action in actions) action.execute(event, menu)
    }

    /**
     * If this menu item does not specify whether or not to allow players to move this item, this allows this encompassing menu to overwrite the value.
     *
     * @param defaultInteractionsBlocked Menu default value
     */
    internal fun applyDefaultInteractionsBlocked(defaultInteractionsBlocked: Boolean) = internalInteractionsBlocked ?: run { interactionsBlocked = defaultInteractionsBlocked }

}