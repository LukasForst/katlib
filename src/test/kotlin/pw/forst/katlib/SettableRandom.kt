package pw.forst.katlib

import java.util.Random

/**
 * Implementation of random class which returns specified values. This is useful mainly for test cases to ensure desired outputs.
 */
internal class SettableRandom : Random(20) {
    private lateinit var nextDoubleValues: DoubleIterator
    private lateinit var nextGaussianValues: DoubleIterator
    private lateinit var nextIntValues: IntIterator

    override fun nextDouble(): Double {
        return nextDoubleValues.nextDouble()
    }

    override fun nextGaussian(): Double {
        return nextGaussianValues.nextDouble()
    }

    override fun nextInt(bound: Int): Int {
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
