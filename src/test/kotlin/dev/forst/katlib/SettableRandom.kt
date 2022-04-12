package dev.forst.katlib

import kotlin.random.Random

/**
 * Implementation of random class which returns specified values. This is useful mainly for test cases to ensure desired outputs.
 */
internal class SettableRandom : Random() {
    private lateinit var nextDoubleValues: DoubleIterator
    private lateinit var nextIntValues: IntIterator

    override fun nextBits(bitCount: Int): Int {
        return nextIntValues.nextInt()
    }

    override fun nextDouble(): Double {
        return nextDoubleValues.nextDouble()
    }

    override fun nextInt(until: Int): Int {
        return nextIntValues.nextInt()
    }

    fun setNextDoubleValues(vararg values: Double): SettableRandom {
        nextDoubleValues = values.iterator()
        return this
    }

    fun setNextIntValues(vararg values: Int): SettableRandom {
        nextIntValues = values.iterator()
        return this
    }

}
