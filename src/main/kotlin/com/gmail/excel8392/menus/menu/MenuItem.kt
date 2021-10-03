package com.gmail.excel8392.menus.menu

import com.gmail.excel8392.menus.action.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.LinkedList
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.isAccessible

class MenuItem @JvmOverloads constructor(
    val icon: ItemStack,
    interactionsBlocked: Boolean? = null) {

    var interactionsBlocked: Boolean by object: ReadWriteProperty<Any?, Boolean> {
        var value: Boolean? = interactionsBlocked
        override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean =
            value ?: throw IllegalStateException("MenuItem#interactionsBlocked not overridden by MenuItem holder!")
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) { this.value = value }
    }

    val actions = LinkedList<Action>()

    fun addAction(action: Action) {
        actions.add(action)
    }

    fun onClick(event: InventoryClickEvent) {
        for (action in actions) action.execute(event)
    }

    fun applyDefaultInteractionsBlocked(defaultInteractionsBlocked: Boolean) {
        val delegate = ::interactionsBlocked.apply { isAccessible = true }.getDelegate()!!
        @Suppress("UNCHECKED_CAST")
        val property = delegate::class.members.first { it.name == "value" } as KProperty1<Any, *>
        property.get(delegate) as Boolean? ?: run { interactionsBlocked = defaultInteractionsBlocked }
    }

}