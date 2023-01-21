package com.gmail.excel8392.menus.generator

import com.gmail.excel8392.menus.builder.MenuBuilder
import org.bukkit.entity.Player

/**
 * Abstract class used to split the building of menus into static elements and dynamic elements.
 * This is used primarily for generating customized menus for each player,
 * where some menu items are static (same for all players) and some change per player.
 *
 * The type parameter for this class represents the type of MenuBuilder that will be created and use to build menus.
 *
 * The initialization of the menu builder and dynamic elements is set to be lazy by default (called on demand),
 * but can be modified using a MenuGeneratorBuilder.
 *
 * This class is not meant to implemented externally, rather use the MenuGeneratorBuilder.
 *
 * @see com.gmail.excel8392.menus.menu.Menu
 * @see com.gmail.excel8392.menus.builder.MenuBuilder
 * @see com.gmail.excel8392.menus.builder.MenuGeneratorBuilder
 */
abstract class MenuGenerator<T: MenuBuilder<T>>(protected val lazy: Boolean = true) {

    // Split delegate into different declaration to avoid using kotlin-reflect to get it
    // WARNING: note that using instance data in generateMenuBuilder or applyStaticElements will result
    // in error if this class loads eagerly!
    /** Lazy delegate that creates a menu builder and applies the static elements */
    private val lazyDelegate = lazy { generateMenuBuilder().also { applyStaticElements(it) } }
    /** MenuBuilder that is cloned to create each menu */
    private val menuBuilder: MenuBuilder<T> by lazyDelegate

    init {
        // Forces lazy delegate to load
        if (!lazy) lazyDelegate.value
    }

    /**
     * Generate a menu builder used to create each menu. Static elements will immediately be applied.
     * This builder is cloned for each player that opens it, but is only generated once.
     *
     * Implementation specification: This method should NOT use any instance data not from super class!
     *
     * @return a new menu builder used for creating the menu
     */
    protected abstract fun generateMenuBuilder(): MenuBuilder<T>

    /**
     * Applies the static elements to the generated menu builder. This method is only called once,
     * immediately after generating the builder.
     *
     * Implementation specification: This method should NOT use any instance data not from super class!
     *
     * @param menuBuilder MenuBuilder to apply static elements to
     */
    protected abstract fun applyStaticElements(menuBuilder: MenuBuilder<T>)

    /**
     * Applies the dynamic elements to a cloned menu builder. This method is called every time a player opens the menu.
     *
     * @param menuBuilder MenuBuilder to apply dynamic elements to
     */
    protected abstract fun applyDynamicElements(menuBuilder: MenuBuilder<T>, viewer: Player)

    /**
     * Generate and open a menu for a player. Immediately applies dynamic elements to a cloned
     * MenuBuilder and then opens the menu.
     *
     * @param viewer Player to open the menu for
     * @return Menu generated for that player
     */
    fun openMenu(viewer: Player) = menuBuilder.clone().also {
            applyDynamicElements(it, viewer)
        }.build().apply { openMenu(viewer) }

}