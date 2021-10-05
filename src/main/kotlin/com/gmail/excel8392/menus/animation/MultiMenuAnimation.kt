package com.gmail.excel8392.menus.animation

import com.gmail.excel8392.menus.menu.Menu
import com.gmail.excel8392.menus.util.NumberUtil
import org.bukkit.entity.Player

class MultiMenuAnimation(val animations: List<MenuAnimation>): MenuAnimation {

    override val interval = NumberUtil.gcd(*animations.stream().mapToInt { it.interval }.toArray())

    private val intervalCounter = HashMap<MenuAnimation, Long>()

    override fun tickAnimation(viewer: Player, menu: Menu) {
        for (animation in animations) {
            intervalCounter[animation] = intervalCounter[animation]!! + interval
            if (intervalCounter[animation]!! >= animation.interval) {
                intervalCounter[animation] = intervalCounter[animation]!! - animation.interval
                animation.tickAnimation(viewer, menu)
            }
        }
    }

}