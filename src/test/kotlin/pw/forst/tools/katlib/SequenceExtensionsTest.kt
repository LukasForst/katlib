package pw.forst.tools.katlib

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SequenceExtensionsTest {

    @Test
    fun testAssoc() {
        val input = sequenceOf(1 to 2, 3 to 4, 3 to 5)
        val expected = mapOf(1 to 2, 3 to 5)
        assertEquals(expected, input.assoc())
    }

    @Test
    fun testAssocPair() {
        val input = sequenceOf(1 to 2, 3 to 4, 3 to 5)
        val expected = mapOf(1 to 2, 3 to 5)
        assertEquals(expected, input.assoc { it })
    }

    @Test
    fun testAssocByKey() {
        val input = sequenceOf(1 to 2, 3 to 4, 3 to 5)
        val expected = mapOf(1 to Pair(1, 2), 3 to Pair(3, 5))
        assertEquals(expected, input.assocBy { it.first })
    }

    @Test
    fun testAssocBy() {
        val input = sequenceOf(1 to 2, 3 to 4, 3 to 5)
        val expected = mapOf(1 to 2, 3 to 5)
        assertEquals(expected, input.assocBy({ it.first }, { it.second }))
    }

    @Test
    fun testAssocWith() {
        val input = sequenceOf(1 to 2, 3 to 5, 3 to 5)
        val expected = mapOf(Pair(1, 2) to 2, Pair(3, 5) to 5)
        assertEquals(expected, input.assocWith { it.second })
    }
}
