package com.gmail.excel8392.menus.animation

import com.gmail.excel8392.menus.menu.Menu
import com.gmail.excel8392.menus.util.NumberUtil
import org.bukkit.entity.Player

/**
 * A MenuAnimation that can hold multiple other menu animations and executes them as one.
 * This is used to consolidate multiple MenuItemAnimations into a single animation that can be displayed on different menus.
 *
 * @see com.gmail.excel8392.menus.animation.MenuAnimation
 * @see com.gmail.excel8392.menus.animation.MenuItemAnimation
 * @see com.gmail.excel8392.menus.menu.Menu
 */
class MultiMenuAnimation(val animations: List<MenuAnimation>): MenuAnimation {

    /**
     * The animation interval, calculated as the GCD of all the sub-animation's intervals
     * This is done so that we can run multiple animations with different intervals using a single bukkit repeating task.
     */
    override val interval = NumberUtil.gcd(*animations.stream().mapToInt { it.interval }.toArray())

    /**
     * This counter is used to count up to each interval for each animation we are running with our single repeating task.
     * The long represents the number of ticks that have passed since last execution.
     * When the counter passes the key animation's interval in value, the animation is ticked and the counter reset.
     */
    private val intervalCounter = HashMap<MenuAnimation, Long>()

    /**
     * MultiMenuAnimation implementation of the tick animation using our interval counter for each menu animation.
     * @see intervalCounter
     */
    override fun tickAnimation(viewer: Player, menu: Menu) {
        for (animation in animations) {
            // Increment the interval counter
            intervalCounter[animation] = intervalCounter[animation]!! + interval
            // Check if this animation's counter has reached the animation's interval
            if (intervalCounter[animation]!! >= animation.interval) {
                // Reset the counter
                intervalCounter[animation] = intervalCounter[animation]!! - animation.interval
                // Tick animation
                animation.tickAnimation(viewer, menu)
            }
        }
    }

}