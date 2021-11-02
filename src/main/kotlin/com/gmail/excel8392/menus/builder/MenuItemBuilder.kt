package com.gmail.excel8392.menus.builder

import com.gmail.excel8392.menus.action.MenuAction
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
import java.util.LinkedList

/**
 * Builder pattern class allowing you to construct a MenuItem.
 *
 * @constructor Create a MenuItem icon from a material and an amount.
 *
 * @param material The material of the icon for this MenuItem
 * @param amount The amount of the item icon for this MenuItem
 * @property colorPrefix Color code prefix for translating alternate color codes
 *
 * @see com.gmail.excel8392.menus.menu.MenuItem
 */
open class MenuItemBuilder @JvmOverloads constructor(
    material: Material,
    amount: Int = 1,
    private val colorPrefix: Char = '&'
): Cloneable {

    /** The item stack used to represent this icon */
    private var item: ItemStack = ItemStack(material, amount)
    /** The item meta applied to the item on build */
    private var meta: ItemMeta? = item.itemMeta
    /** Determines if item is movable in inventory, null means it will take the menu default */
    private var interactionsBlocked: Boolean? = null

    /** List of actions to execute on icon click in menu */
    private var actions = LinkedList<MenuAction>()

    /**
     * Create a MenuItem icon from a material, amount and custom display name.
     *
     * @param material The material of the icon for this MenuItem
     * @param amount The amount of the item icon for this MenuItem
     * @param name The custom display name of the item icon for this MenuItem (supports color codes)
     * @param colorPrefix Color code prefix for translating alternate color codes
     */
    @JvmOverloads
    constructor(material: Material, amount: Int, name: String, colorPrefix: Char = '&'): this(material, amount, colorPrefix = colorPrefix) {
        setName(name)
    }

    /**
     * Create a MenuItem icon from a material, amount, custom display name, and custom lore.
     *
     * @param material The material of the icon for this MenuItem
     * @param amount The amount of the item icon for this MenuItem
     * @param name The custom display name of the item icon for this MenuItem (supports color codes)
     * @param lore The custom lore of the item icon for this MenuItem (supports color codes)
     * @param colorPrefix Color code prefix for translating alternate color codes
     */
    @JvmOverloads
    constructor(material: Material, amount: Int, name: String, vararg lore: String, colorPrefix: Char = '&'): this(material, amount, colorPrefix = colorPrefix) {
        setName(name)
        setLore(*lore)
    }

    /**
     * Add actions to execute on click for this menu item.
     *
     * @param actions Vararg of actions that will be executed
     * @return This builder for use in the builder pattern
     */
    fun addActions(vararg actions: MenuAction): MenuItemBuilder {
        for (action in actions) this.actions.add(action)
        return this
    }

    /**
     * Set the custom display name for the ItemStack icon representing this menu item.
     *
     * @param name Custom display name (supports color codes)
     * @return This builder for use in the builder pattern
     */
    fun setName(name: String): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot set name for item with null meta!")
        meta!!.setDisplayName(ChatColor.translateAlternateColorCodes(colorPrefix, if (name.isNotEmpty()) name else " "))
        return this
    }

    /**
     * Set the custom lore for the ItemStack icon representing this menu item.
     *
     * @param lore Custom lore vararg (supports color codes)
     * @return This builder for use in the builder pattern
     */
    fun setLore(vararg lore: String): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot set lore for item with null meta!")
        val existingLore = ArrayList<String>(lore.size)
        for (line in lore) existingLore.add(ChatColor.translateAlternateColorCodes(colorPrefix, line))
        meta!!.lore = existingLore
        return this
    }

    /**
     * Set the custom lore for the ItemStack icon representing this menu item.
     *
     * @param lore Custom lore list (supports color codes)
     * @return This builder for use in the builder pattern
     */
    fun setLore(lore: List<String>): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot set lore for item with null meta!")
        val newLore = ArrayList<String>(lore.size)
        for (line in lore) newLore.add(ChatColor.translateAlternateColorCodes(colorPrefix, line))
        meta!!.lore = newLore
        return this
    }

    /**
     * Appends lines of custom lore for the ItemStack icon representing this menu item.
     *
     * @param lore Custom lore vararg (supports color codes)
     * @return This builder for use in the builder pattern
     */
    fun addLore(vararg lore: String): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot add lore for item with null meta!")
        val existingLore = meta!!.lore ?: ArrayList(lore.size)
        for (line in lore) existingLore.add(ChatColor.translateAlternateColorCodes(colorPrefix, line))
        meta!!.lore = existingLore
        return this
    }

    /**
     * Adds item flags to the ItemStack icon representing this menu item.
     *
     * @param flags Custom item flags vararg
     * @return This builder for use in the builder pattern
     */
    fun addFlags(vararg flags: ItemFlag): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot add item flags for item with null meta!")
        meta!!.addItemFlags(*flags)
        return this
    }

    /**
     * Adds an attribute modifier to the ItemStack icon representing this menu item.
     *
     * @param attribute Custom attribute type
     * @param modifier Custom attribute modifier to apply
     * @return This builder for use in the builder pattern
     */
    fun addAttributeModifier(attribute: Attribute, modifier: AttributeModifier): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot add attribute modifiers for item with null meta!")
        meta!!.addAttributeModifier(attribute, modifier)
        return this
    }

    /**
     * Adds attribute modifiers to the ItemStack icon representing this menu item.
     *
     * @param modifiers Pair of attribute type and modifier to apply vararg
     * @return This builder for use in the builder pattern
     */
    fun addAttributeModifiers(vararg modifiers: Pair<Attribute, AttributeModifier>): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot add attribute modifiers for item with null meta!")
        for ((attribute, modifier) in modifiers) meta!!.addAttributeModifier(attribute, modifier)
        return this
    }

    /**
     * Set whether or not the ItemStack icon representing this menu item is unbreakable.
     *
     * @param unbreakable Is unbreakable
     * @return This builder for use in the builder pattern
     */
    fun setUnbreakable(unbreakable: Boolean): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot set unbreakable for item with null meta!")
        meta!!.isUnbreakable = unbreakable
        return this
    }

    /**
     * Add an enchantment to the ItemStack icon representing this menu item.
     *
     * @param enchantment Enchantment to add
     * @param level Level of enchantment
     * @return This builder for use in the builder pattern
     */
    fun addEnchant(enchantment: Enchantment, level: Int): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot add enchant for item with null meta!")
        meta!!.addEnchant(enchantment, level, true)
        return this
    }

    /**
     * Adds multiple enchantments to the ItemStack icon representing this menu item.
     *
     * @param enchants Pair of enchantment types and levels vararg
     * @return This builder for use in the builder pattern
     */
    fun addEnchants(vararg enchants: Pair<Enchantment, Int>): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot add enchants for item with null meta!")
        for ((enchantment, level) in enchants) meta!!.addEnchant(enchantment, level, true)
        return this
    }

    /**
     * Run a custom lambda using the current item meta of the ItemStack representing this menu item.
     * The lambda will be run immediately, and any changes made to the meta will be applied to it.
     *
     * @param consumer Lambda consumer, ItemMeta parameter
     * @return This builder for use in the builder pattern
     */
    fun applyItemMeta(consumer: (ItemMeta) -> Unit): MenuItemBuilder {
        meta ?: throw IllegalStateException("Cannot apply item meta for item with null meta!")
        consumer(meta!!)
        return this
    }

    /**
     * Sets the damage/durability of the ItemStack icon representing this menu item.
     *
     * @param damage Damange/Durability of the item
     * @return This builder for use in the builder pattern
     */
    fun setDamage(damage: Int): MenuItemBuilder {
        if (meta !is Damageable) throw IllegalStateException("Cannot apply damage for non Damageable meta!")
        (meta as Damageable).damage = damage
        return this
    }

    /**
     * Sets the ItemStack icon representing this menu item.
     * This will also completely override the current item meta and replace it with a new one!
     * All changes will be lost.
     *
     * @param item ItemStack icon
     * @return This builder for use in the builder pattern
     */
    fun setItemStack(item: ItemStack): MenuItemBuilder {
        this.item = item
        this.meta = item.itemMeta
        return this
    }

    /**
     * Sets the item meta for the ItemStack icon representing this menu item.
     * Completely overrides the previous item meta and any changes that were made to it.
     *
     * @param meta Item meta for the ItemStack icon
     * @return This builder for use in the builder pattern
     */
    fun setItemMeta(meta: ItemMeta?): MenuItemBuilder {
        this.meta = meta
        return this
    }

    /**
     * Sets if interactions should be blocked on this menu item.
     * This decides if the viewer can drag and drop to remove the icon from the menu.
     * If not set then this will end up being the menu's default.
     *
     * @param interactionsBlocked Should viewer interactions be blocked
     * @return This builder for use in the builder pattern
     */
    fun setInteractionsBlocked(interactionsBlocked: Boolean): MenuItemBuilder {
        this.interactionsBlocked = interactionsBlocked
        return this
    }

    /**
     * Build a menu item from this builder.
     *
     * @return MenuItem
     */
    fun build(): MenuItem {
        item.itemMeta = meta
        val menuItem = MenuItem(item, interactionsBlocked = interactionsBlocked)
        for (action in actions) menuItem.addAction(action)
        return menuItem
    }

    /**
     * Clone this builder.
     * The cloned builder will have the same refrences to the itemstack icon and itemstack meta.
     *
     * @return Cloned builder
     */
    override fun clone(): MenuItemBuilder {
        return MenuItemBuilder(this.item.type).setItemStack(this.item).setItemMeta(this.meta)
    }

}