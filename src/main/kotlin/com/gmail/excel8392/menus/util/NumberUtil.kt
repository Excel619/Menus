package com.gmail.excel8392.menus.util

/**
 * Utility methods for number or math related operations in this library.
 */
internal object NumberUtil {

    /**
     * Calculate the GCD of two numbers.
     *
     * @param a First number
     * @param b Second number
     */
    internal fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

    /**
     * Calculate the GCD of any amount of numbers.
     *
     * @param numbers
     * @return GCD of numbers
     */
    internal fun gcd(vararg numbers: Int): Int {
        var result = 0
        for (element in numbers) {
            result = gcd(result, element)
            if (result == 1) return 1
        }
        return result
    }

    /**
     * Calculate the GCD of any amount of numbers.
     *
     * @param numbers
     * @return GCD of numbers
     */
    internal fun gcd(numbers: Collection<Int>): Int {
        var result = 0
        for (element in numbers) {
            result = gcd(result, element)
            if (result == 1) return 1
        }
        return result
    }

}