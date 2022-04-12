package dev.forst.katlib

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PairExtensionsKtTest {
    @Test
    fun `mapLeft test`() {
        val pair = Pair(listOf(10), 0)
        val expected = Pair(listOf(20), 0)
        assertEquals(expected, pair.mapLeft { it * 2 })
    }

    @Test
    fun `mapRight test`() {
        val pair = Pair(0, listOf(10))
        val expected = Pair(0, listOf(20))
        assertEquals(expected, pair.mapRight { it * 2 })
    }

    @Test
    fun `mapPair test`() {
        val pair = Pair(listOf(1), listOf(10))
        val expected = Pair(listOf("1"), listOf("20"))
        assertEquals(expected, pair.mapPair({ it.toString() }, { (it * 2).toString() }))
    }

    @Test
    fun `letLeft test`() {
        val pair = Pair("hello", 0)
        val expected = Pair("hello world", 0)
        assertEquals(expected, pair.letLeft { "$it world" })
    }

    @Test
    fun `letRight test`() {
        val pair = Pair("hello", 1)
        val expected = Pair("hello", 11)
        assertEquals(expected, pair.letRight { it + 10 })
    }

    @Test
    fun `letPair test`() {
        val pair = Pair("hello", "world")
        val expected = Pair("hello 5", "wo")
        assertEquals(expected, pair.letPair({ "$it 5" }, { it.take(2) }))
    }
}
