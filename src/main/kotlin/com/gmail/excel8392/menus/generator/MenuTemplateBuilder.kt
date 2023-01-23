package com.gmail.excel8392.menus.generator

import com.gmail.excel8392.menus.builder.MenuBuilder
import org.bukkit.entity.Player

/**
 * Builder pattern class allowing API users to construct a menu generator.
 *
 * @constructor Create a MenuGeneratorBuilder from a MenuBuilder and a lazy declaration.
 *
 * @param T Type parameter representing the type of menu builder that the menu generator will hold
 * @property menuBuilder The initial menu builder that the built generator will hold and make copies of to generate menus
 * @property lazy Declare whether this built generator will be lazy or eager, defaults to true
 *
 * @see com.gmail.excel8392.menus.builder.MenuBuilder
 * @see com.gmail.excel8392.menus.generator.MenuTemplate
 * @see com.gmail.excel8392.menus.menu.Menu
 */
class MenuTemplateBuilder<T: MenuBuilder<T>> @JvmOverloads constructor(var menuBuilder: T, var lazy: Boolean = true) {

    // TODO document

    private var staticGenerator: ((T) -> Unit)? = null
    private var dynamicGenerator: ((T, Player) -> Unit)? = null

    fun setMenuBuilder(menuBuilder: T): MenuTemplateBuilder<T> {
        this.menuBuilder = menuBuilder
        return this
    }

    fun setLazy(lazy: Boolean): MenuTemplateBuilder<T> {
        this.lazy = lazy
        return this
    }

    fun setStaticElementGenerator(staticGenerator: (T) -> Unit): MenuTemplateBuilder<T> {
        this.staticGenerator = staticGenerator
        return this
    }

    fun setDynamicElementGenerator(dynamicGenerator: (T, Player) -> Unit): MenuTemplateBuilder<T> {
        this.dynamicGenerator = dynamicGenerator
        return this
    }

    fun build() = object: MenuTemplate<T>(lazy = lazy) {
        // Save reference to template and not the builder so that it can be garbage collected
        // Type erasure may cause some issues, be careful
        val staticGenerator: ((T) -> Unit)? = this@MenuTemplateBuilder.staticGenerator
        val dynamicGenerator: ((T, Player) -> Unit)? = this@MenuTemplateBuilder.dynamicGenerator
        val menuBuilder: T = this@MenuTemplateBuilder.menuBuilder

        override fun generateMenuBuilder() = menuBuilder
        override fun applyStaticElements(menuBuilder: T): Unit = run { staticGenerator?.invoke(menuBuilder) }
        override fun applyDynamicElements(menuBuilder: T, viewer: Player): Unit = run { dynamicGenerator?.invoke(menuBuilder, viewer) }
    }

}