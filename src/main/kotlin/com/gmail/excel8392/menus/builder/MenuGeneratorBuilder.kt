package com.gmail.excel8392.menus.builder

import com.gmail.excel8392.menus.generator.MenuGenerator
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
 * @see com.gmail.excel8392.menus.generator.MenuGenerator
 * @see com.gmail.excel8392.menus.menu.Menu
 */
class MenuGeneratorBuilder<T: MenuBuilder<T>> @JvmOverloads constructor(var menuBuilder: MenuBuilder<T>, var lazy: Boolean = true) {

    /** */
    private var staticGenerator: ((MenuBuilder<T>) -> Unit)? = null
    private var dynamicGenerator: ((MenuBuilder<T>, Player) -> Unit)? = null

    fun setMenuBuilder(menuBuilder: MenuBuilder<T>): MenuGeneratorBuilder<T> {
        this.menuBuilder = menuBuilder
        return this
    }

    fun setLazy(lazy: Boolean): MenuGeneratorBuilder<T> {
        this.lazy = lazy
        return this
    }

    fun setStaticElementGenerator(staticGenerator: (MenuBuilder<T>) -> Unit): MenuGeneratorBuilder<T> {
        this.staticGenerator = staticGenerator
        return this
    }

    fun setDynamicElementGenerator(dynamicGenerator: (MenuBuilder<T>, Player) -> Unit): MenuGeneratorBuilder<T> {
        this.dynamicGenerator = dynamicGenerator
        return this
    }

    fun build() = object: MenuGenerator<T>(lazy = lazy) {
        // Save reference to generators and not the builder so that it can be garbage collected
        // Type erasure may cause some issues, be careful
        val staticGenerator: ((MenuBuilder<T>) -> Unit)? = this@MenuGeneratorBuilder.staticGenerator
        val dynamicGenerator: ((MenuBuilder<T>, Player) -> Unit)? = this@MenuGeneratorBuilder.dynamicGenerator
        val menuBuilder: MenuBuilder<T> = this@MenuGeneratorBuilder.menuBuilder

        override fun generateMenuBuilder() = menuBuilder
        override fun applyStaticElements(menuBuilder: MenuBuilder<T>): Unit = run { staticGenerator?.invoke(menuBuilder) }
        override fun applyDynamicElements(menuBuilder: MenuBuilder<T>, viewer: Player): Unit = run { dynamicGenerator?.invoke(menuBuilder, viewer) }
    }

}