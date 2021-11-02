package com.gmail.excel8392.menus.menu

import com.gmail.excel8392.menus.action.MenuAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.LinkedList
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class MenuItem @JvmOverloads constructor(
    val icon: ItemStack,
    interactionsBlocked: Boolean? = null
) {

    private var internalInteractionsBlocked: Boolean? = interactionsBlocked

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