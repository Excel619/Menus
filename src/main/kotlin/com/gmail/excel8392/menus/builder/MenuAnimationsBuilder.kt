package com.gmail.excel8392.menus.builder

import com.gmail.excel8392.menus.animation.MenuAnimation
import com.gmail.excel8392.menus.animation.MultiMenuAnimation
import java.util.LinkedList

class MenuAnimationsBuilder {

    private val animations = LinkedList<MenuAnimation>()

    fun addAnimation(animation: MenuAnimation) = animations.add(animation)

    fun build() = MultiMenuAnimation(animations)

    fun buildList() = animations.toList() // Converts our collection to be immutable

    fun apply(menu: MenuBuilder) = menu.addAnimation(build())

}