package com.gmail.excel8392.menus.builder

import com.gmail.excel8392.menus.menu.MenuItem
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.ItemMeta

class MenuItemBuilder @JvmOverloads constructor(
    material: Material,
    amount: Int = 1,
    private val colorPrefix: Char = '&'
): Cloneable {

    private var item: ItemStack = ItemStack(material, amount)
    private var meta: ItemMeta? = item.itemMeta
    private var interactionsBlocked: Boolean? = null

    fun setName(name: String): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot set name for item with null meta!")
        meta!!.setDisplayName(ChatColor.translateAlternateColorCodes(colorPrefix, name))
        return this
    }

    fun setLore(vararg lore: String): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot set lore for item with null meta!")
        val existingLore = ArrayList<String>(lore.size)
        for (line in lore) existingLore.add(ChatColor.translateAlternateColorCodes(colorPrefix, line))
        meta!!.lore = existingLore
        return this
    }

    fun setLore(lore: List<String>): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot set lore for item with null meta!")
        val newLore = ArrayList<String>(lore.size)
        for (line in lore) newLore.add(ChatColor.translateAlternateColorCodes(colorPrefix, line))
        meta!!.lore = newLore
        return this
    }

    fun addLore(vararg lore: String): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot add lore for item with null meta!")
        val existingLore = meta!!.lore ?: ArrayList(lore.size)
        for (line in lore) existingLore.add(ChatColor.translateAlternateColorCodes(colorPrefix, line))
        meta!!.lore = existingLore
        return this
    }

    fun addFlags(vararg flags: ItemFlag): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot add item flags for item with null meta!")
        meta!!.addItemFlags(*flags)
        return this
    }

    fun addAttributeModifiers(attribute: Attribute, modifier: AttributeModifier): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot add attribute modifiers for item with null meta!")
        meta!!.addAttributeModifier(attribute, modifier)
        return this
    }

    fun addAttributeModifiers(vararg modifiers: Pair<Attribute, AttributeModifier>): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot add attribute modifiers for item with null meta!")
        for ((attribute, modifier) in modifiers) meta!!.addAttributeModifier(attribute, modifier)
        return this
    }

    fun setUnbreakable(unbreakable: Boolean): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot set unbreakable for item with null meta!")
        meta!!.isUnbreakable = unbreakable
        return this
    }

    fun addEnchant(enchantment: Enchantment, level: Int): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot add enchant for item with null meta!")
        meta!!.addEnchant(enchantment, level, true)
        return this
    }

    fun addEnchants(vararg enchants: Pair<Enchantment, Int>): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot add enchants for item with null meta!")
        for ((enchantment, level) in enchants) meta!!.addEnchant(enchantment, level, true)
        return this
    }

    fun applyItemMeta(consumer: (ItemMeta) -> Unit): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot apply item meta for item with null meta!")
        consumer(meta!!)
        return this
    }

    fun setDamage(damage: Int): MenuItemBuilder {
        if (meta !is Damageable) throw IllegalStateException("Cannot apply damage for non Damageable meta!")
        (meta as Damageable).damage = damage
        return this
    }

    fun setItemStack(item: ItemStack): MenuItemBuilder {
        this.item = item
        return this
    }

    fun setItemMeta(meta: ItemMeta?): MenuItemBuilder {
        this.meta = meta
        return this
    }

    fun setInteractionsBlocked(interactionsBlocked: Boolean): MenuItemBuilder {
        this.interactionsBlocked = interactionsBlocked
        return this
    }

    fun build(): MenuItem {
        item.itemMeta = meta
        return MenuItem(item, interactionsBlocked = interactionsBlocked)
    }

    override fun clone(): MenuItemBuilder {
        return MenuItemBuilder(this.item.type).setItemStack(this.item).setItemMeta(this.meta)
    }

}