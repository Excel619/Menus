package com.gmail.excel8392.menus.builder

import com.gmail.excel8392.menus.animation.MenuAnimation
import com.gmail.excel8392.menus.animation.MultiMenuAnimation
import java.util.LinkedList

/**
 * Builder pattern class allowing you to construct a MenuAnimation made of multiple menu animations.
 * Alternatively you can use #buildList() to create a list of the animations instead of them being consolidating into a single animation.
 *
 * @constructor Create empty Menu animations builder
 *
 * @see com.gmail.excel8392.menus.animation.MenuAnimation
 * @see com.gmail.excel8392.menus.animation.MultiMenuAnimation
 * @see com.gmail.excel8392.menus.menu.Menu
 */
open class MenuAnimationsBuilder {

    /** LinkedList of animations for this class */
    private val animations = LinkedList<MenuAnimation>()

    /**
     * Add an animation to this builder.
     *
     * @param animation The animation to add
     * @return This builder for use in the builder pattern
     */
    fun addAnimation(animation: MenuAnimation): MenuAnimationsBuilder {
        animations.add(animation)
        return this
    }

    /**
     * Build a MultiMenuAnimation containing all the animations of this builder.
     *
     * @return A built MultiMenuAnimation
     */
    fun build() = MultiMenuAnimation(animations)

    /**
     * Builds an immutable list of the animations contained in this class.
     *
     * @return Immutable list of animations
     */
    fun buildList() = animations.toList() // Converts our collection to be immutable

    /**
     * Apply these animations to a MenuBuilder.
     * This simply sets the MenuBuilder's animations to this builder's current animations.
     *
     * @param menuBuilder MenuBuilder to modify
     */
    fun apply(menuBuilder: MenuBuilder<*>) = menuBuilder.addAnimation(build())

}