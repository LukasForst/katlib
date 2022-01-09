@file:Suppress("DEPRECATION") // we want to keep tests even for deprecated code

package pw.forst.katlib

import org.junit.jupiter.api.Test
import java.util.Locale
import java.util.TreeSet
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

internal class IterableExtensionsTest {

    @Test
    fun toNavigableSetTest() {
        val iterable = listOf(1, 2, 10, 0, 3, 3)
        val expected = TreeSet(iterable)
        val actual = iterable.toNavigableSet(Comparator.naturalOrder())

        assertEquals(expected, actual)
    }

    @Test
    @Suppress("DEPRECATION")
    fun testSumByLong() {
        val longs = listOf(10L, 20L, 30L)
        assertEquals(120L, longs.sumByLong { it * 2 })
        val empty = listOf<Long>()
        assertEquals(0, empty.sumByLong { it * 2 })
    }

    @Test
    @Suppress("DEPRECATION")
    fun testSumByLongSequence() {
        val longs = sequenceOf(10L, 20L, 30L)
        assertEquals(120L, longs.sumByLong { it * 2 })
        val empty = sequenceOf<Long>()
        assertEquals(0, empty.sumByLong { it * 2 })
    }

    @Test
    fun testGetRandomElement() {
        val items = listOf("0", "1", "2", "3", "4")
        val rand = SettableRandom()
        rand.setNextIntValues(0, 3, 2, 1, 4)
        assertEquals("0", items.getRandomElement(rand))
        assertEquals("3", items.getRandomElement(rand))
        assertEquals("2", items.getRandomElement(rand))
        assertEquals("1", items.getRandomElement(rand))
        assertEquals("4", items.getRandomElement(rand))
        try {
            repeat(100) { items.getRandomElement(Random) }
        } catch (e: Exception) {
            fail("this method should not throw with default random class. Exception is $e")
        }
    }

    @Test
    fun testReduction() {
        val values = listOf(1, 5, 7, 3, 1, 8)
        val cumSum = values.reduction(0) { a, b -> a + b }
        assertEquals(listOf(1, 6, 13, 16, 17, 25), cumSum)
        val cumProd = values.reduction(1) { a, b -> a * b }
        assertEquals(listOf(1, 5, 35, 105, 105, 840), cumProd)
    }

    @Test
    fun testSumByIndexes() {
        val empty: Iterable<List<Int>> = listOf()
        assertFailsWith<Exception> { empty.sumByIndexes() }

        val single = listOf(List(4) { it })
        assertEquals(single.first(), single.sumByIndexes())

        val multiple = listOf(List(5) { 2 * it + 1 }, List(4) { it }, List(4) { 1 })
        val result = listOf(2, 5, 8, 11)
        assertEquals(result, multiple.sumByIndexes())
    }

    @Test
    fun testMaxValueBy() {
        val empty = listOf<Int>()
        assertNull(empty.maxValueBy { it })

        val data = listOf(1 to "a", 5 to "c", 5 to "a", 0 to "l")
        assertEquals(5, data.maxValueBy { it.first })
        assertEquals("l", data.maxValueBy { it.second })
    }

    @Test
    fun testMinValueBy() {
        val empty = listOf<Int>()
        assertNull(empty.minValueBy { it })

        val data = listOf(1 to "a", 5 to "c", 5 to "a", 0 to "l")
        assertEquals(0, data.minValueBy { it.first })
        assertEquals("a", data.minValueBy { it.second })
    }

    @Test
    fun testDominantValueBy() {
        val empty = listOf<Int>()
        assertNull(empty.dominantValueBy { it })

        val data = listOf(1 to "a", 5 to "c", 5 to "a", 0 to "l")
        assertEquals(5, data.dominantValueBy { it.first })
        assertEquals("a", data.minValueBy { it.second })
    }

    @Test
    fun testCartesianProduct() {
        val input1 = listOf(1, 2, 3)
        val input2 = listOf('a', 'b')
        val output = setOf(Pair(1, 'a'), Pair(1, 'b'), Pair(2, 'a'), Pair(2, 'b'), Pair(3, 'a'), Pair(3, 'b'))
        assertEquals(output, input1.cartesianProduct(input2))
    }

    @Test
    fun testForEachNotNull() {
        val result = ArrayList<Int>()
        val input = listOf(1, 2, null, 3, 4, null)
        val expected = listOf(1, 2, 3, 4)
        input.forEachNotNull { result.add(it) }
        assertEquals(expected, result)
    }

    @Test
    fun testUnion() {
        val input1 = listOf(1, 2, 3)
        val input2 = listOf(3, 4, 5)
        val input3 = listOf(5, 6, 7)
        val output = setOf(1, 2, 3, 4, 5, 6, 7)
        assertEquals(output, listOf(input1, input2, input3).union())
    }

    @Test
    fun testIntersect() {
        val input1 = listOf(1, 2, 3)
        val input2 = listOf(3, 4, 5)
        val input3 = listOf(5, 3, 7)
        val output = setOf(3)
        assertEquals(output, listOf(input1, input2, input3).intersect())
    }

    @Test
    fun testFilterNotNullBy() {
        val input = listOf(1 to 2, null, 2 to null, 3 to 4, 5 to 6)
        val output = listOf(1 to 2, 3 to 4, 5 to 6)
        assertEquals(output, input.filterNotNullBy { it.second })
    }

    @Test
    fun testSingleOrEmpty() {
        val input = listOf(1, 2, 3, 4, 5)
        assertEquals(5, input.singleOrEmpty { it > 4 })
        assertNull(input.singleOrEmpty { it > 5 })
        assertFailsWith(IllegalArgumentException::class) { input.singleOrEmpty { it > 3 } }

        assertEquals(1, listOf(1).singleOrEmpty())
        assertNull(emptyList<Int>().singleOrEmpty())
        assertFailsWith(IllegalArgumentException::class) { input.singleOrEmpty() }
    }

    @Test
    fun testSplitPairCollection() {
        val input = listOf(1 to 2, 3 to 4, 5 to 6)
        val (odd, even) = input.splitPairCollection()
        assertEquals(listOf(1, 3, 5), odd)
        assertEquals(listOf(2, 4, 6), even)
    }

    @Test
    fun testSetDifferenceBy() {
        val `this` = listOf(1 to 1, 2 to 1, 3 to 1)
        val other = listOf(1 to 2, 2 to 2, 5 to 2)
        val result = `this`.setDifferenceBy(other) { it.first }
        assertTrue { result.size == 1 }
        assertTrue { result.single() == 3 to 1 }
    }

    @Test
    fun testAssoc() {
        val input = listOf(1 to 2, 3 to 4, 3 to 5)
        val expected = mapOf(1 to 2, 3 to 5)
        assertEquals(expected, input.assoc())
    }

    @Test
    fun testAssocPair() {
        val input = listOf(1 to 2, 3 to 4, 3 to 5)
        val expected = mapOf(1 to 2, 3 to 5)
        assertEquals(expected, input.assoc { it })
    }

    @Test
    fun testAssocByKey() {
        val input = listOf(1 to 2, 3 to 4, 3 to 5)
        val expected = mapOf(1 to Pair(1, 2), 3 to Pair(3, 5))
        assertEquals(expected, input.assocBy { it.first })
    }

    @Test
    fun testAssocBy() {
        val input = listOf(1 to 2, 3 to 4, 3 to 5)
        val expected = mapOf(1 to 2, 3 to 5)
        assertEquals(expected, input.assocBy({ it.first }, { it.second }))
    }

    @Test
    fun testAssocWith() {
        val input = listOf(1 to 2, 3 to 5, 3 to 5)
        val expected = mapOf(Pair(1, 2) to 2, Pair(3, 5) to 5)
        assertEquals(expected, input.assocWith { it.second })
    }

    @Test
    fun testFlattenToLists() {
        val input = (1..9).map { Triple(it, it * 10, it * 100) }
        val expected = Triple((1..9).toList(), (1..9).map { it * 10 }, (1..9).map { it * 100 })

        assertEquals(expected, input.flattenToLists())
    }

    @Test
    fun itemsToString() {
        val itemToString: (Int) -> String = { i -> "NUM$i" }
        val result = listOf(10, 20).itemsToString("numbers", itemToString = itemToString)
        assertEquals("2 numbers: NUM10, NUM20", result)
    }

    @Test
    fun foldValidatedShouldVerifyNumbersIncrementByOne() {
        val incrementingNumbers = listOf(2, 3, 4, 5, 6)
        val differenceIsMaxOne = incrementingNumbers.foldValidated { previousNumber, nextNumber ->
            nextNumber - previousNumber == 1
        }

        assertEquals(true, differenceIsMaxOne)
    }

    @Test
    fun foldValidatedShouldEarlyReturnOnFirstFalse() {
        val names = listOf("Dan", "Opie", "Eli")

        val processedNames = mutableListOf<String>().apply {
            names.foldValidated { current, next ->
                add(current)

                current.lowercase(Locale.getDefault()).any { currentChar ->
                    currentChar in next.lowercase(Locale.getDefault())
                }
            }
        }

        val processingStoppedAfterFirstName = names[0] in processedNames && names[1] !in processedNames

        assertEquals(true, processingStoppedAfterFirstName)
    }

    @Test
    fun flatMapIndexedNotNull() {
        val numbers = listOf(-1, 2, 5)
        val finalElement = numbers.flatMapIndexedNotNull { index, current ->
            listOf(current).takeIf { index == 2 }
        }

        assertEquals(1, finalElement.size)
    }

    @Test
    fun flatMapIndexedNotNullFiltersOutNulls() {
        val numbersWithNulls = listOf(2, 3, null, 8, 11, null)
        val flattenedNumbersWithoutNulls = numbersWithNulls.flatMapIndexedNotNull { _, i ->
            if (i != null) listOf(i) else null
        }

        assertEquals(false, flattenedNumbersWithoutNulls.size == numbersWithNulls.size)
    }

    @Test
    fun `test cartesianProduct on list list`() {
        val input = listOf(
            listOf(1, 3, 5),
            listOf(2),
            listOf(4, 6)
        )
        val expected = listOf(
            listOf(1, 2, 4),
            listOf(1, 2, 6),
            listOf(3, 2, 4),
            listOf(3, 2, 6),
            listOf(5, 2, 4),
            listOf(5, 2, 6)
        )
        val actual = input.cartesianProduct()
        assertEquals(expected, actual)

        val actualLazy = input.lazyCartesianProduct()
        assertEquals(expected, actualLazy.toList())
    }

    @Test
    fun `test cartesianProduct with empty list`() {
        val input = listOf(
            listOf(1, 3, 5),
            listOf(),
            listOf(4, 6)
        )
        val expected = listOf<List<Int>>()
        val actual = input.cartesianProduct()
        assertEquals(expected, actual)

        val actualLazy = input.lazyCartesianProduct()
        assertEquals(expected, actualLazy.toList())
    }

    @Test
    fun `test Iterable isEmpty`() {
        assertFalse { Iterable { listOf(1, 2, 3).iterator() }.isEmpty() }
        assertFalse { (listOf(1, 2, 3) as Iterable<Int>).isEmpty() }
        assertTrue { Iterable { emptyList<Int>().iterator() }.isEmpty() }
    }

    @Test
    fun `test zip for three collections`() {
        val a = (0..3).toList()
        val b = (4..7).toList()
        val c = (8..10).toList()

        assertEquals(4, a.size)
        assertEquals(4, b.size)
        assertEquals(3, c.size)

        val expected = (0..2).map { "$it|${it + 4}|${it + 8}" }
        assertEquals(3, expected.size)

        val result = a.zip(b, c) { eA, eB, eC -> "$eA|$eB|$eC" }

        assertEquals(3, result.size)
        assertEquals(expected, result)
    }

    @Test
    fun `test sumByFloat`() {
        val floats = listOf(10f, 20f, 30f)
        assertEquals(120f, floats.sumByFloat { it * 2f })

        val empty = listOf<Float>()
        assertEquals(0f, empty.sumByFloat { it * 2f })
    }
}
