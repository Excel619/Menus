package com.gmail.excel8392.menus.menu

import com.gmail.excel8392.menus.action.MenuAction
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

open class ShopMenuItem constructor(
    icon: ItemStack,
    val condition: ShopCondition,
    val transactions: List<ShopTransaction>,
): MenuItem(icon, true) {

    constructor(
        icon: ItemStack,
        condition: ShopCondition,
        vararg transactions: ShopTransaction = arrayOf()
    ): this(icon, condition, listOf(*transactions))

    init {
        addAction(object: MenuAction {
            override fun execute(event: InventoryClickEvent, menu: Menu) {
                val player = event.whoClicked as? Player ?: return
                val conditionMet = condition.checkCondition(player, menu)
                if (conditionMet) {
                    for (transaction in transactions) {
                        val transactionSucceeded = transaction.applyTransaction(player, menu)
                        if (!transactionSucceeded) {
                            throw RuntimeException("Transaction failed: ${icon.type}, ${player.uniqueId}, ${menu.title}")
                        }
                    }
                }
            }
        })
    }

}