package dev.forst.katlib

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class SetExtensionsTest {

    @Test
    fun minTest() {
        val set = sortedSetOf(1, 10, 12, 0, -1)
        assertEquals(-1, set.min())

        val emptySet = sortedSetOf<Int>()
        assertNull(emptySet.min())
    }

    @Test
    fun maxTest() {
        val set = sortedSetOf(1, 10, 12, 0, -1)
        assertEquals(12, set.max())

        val emptySet = sortedSetOf<Int>()
        assertNull(emptySet.max())
    }
}
