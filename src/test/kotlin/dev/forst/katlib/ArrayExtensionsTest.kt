package dev.forst.katlib

import kotlin.test.Test
import kotlin.test.assertContentEquals

class ArrayExtensionsTest {

	@Test
	fun `buildArray Test`() {
		val builtArray = buildArray {
			for (i in 0 until 100) {
				add(i)
			}
		}
		val constructedArray = Array(100) { it }
		assertContentEquals(builtArray, constructedArray)
	}

	@Test
	fun `Array map Test`() {
		val mappedArray = Array(100) { it }.map { it * 2 }
		val constructedArray = Array(100) { it * 2 }
		assertContentEquals(mappedArray, constructedArray)
	}

	@Test
	fun `Array mapIndexed Test`() {
		val mappedArray = Array(100) { 0 }.mapIndexed { index: Int, _: Int -> index }
		val constructedArray = Array(100) { it }
		assertContentEquals(mappedArray, constructedArray)
	}

	@Test
	fun `Array filter Test`() {
		val filteredArray = Array(100) { it }.filter { it % 2 == 0 }
		val constructedArray = Array(50) { it * 2 }
		assertContentEquals(filteredArray, constructedArray)
	}

	@Test
	fun `Array filterNot Test`() {
		val filteredArray = Array(100) { it }.filterNot { it % 2 != 0 }
		val constructedArray = Array(50) { it * 2 }
		assertContentEquals(filteredArray, constructedArray)
	}

	@Test
	fun `Array filterIndexed Test`() {
		val filteredArray = Array(100) { it }.filterIndexed { index, _ -> index % 2 == 0 }
		val constructedArray = Array(50) { it * 2 }
		assertContentEquals(filteredArray, constructedArray)
	}

	@Test
	fun `Array filterIsInstance Test`() {
		val filteredArray = arrayOf<Number>(0, 1, 0f, 0.0, 1L).filterIsInstance<Int>()
		val constructedArray = Array(2) { it }
		assertContentEquals(filteredArray, constructedArray)
	}

	@Test
	fun `Array filterNotNull Test`() {
		val filteredArray = arrayOf(null, 0, 1, null).filterNotNull()
		val constructedArray = Array(2) { it }
		assertContentEquals(filteredArray, constructedArray)
	}

	@Test
	fun `Array minus element Test`()
	{
		val arrayA = Array(100) { it }
		val arrayB = Array(100) { it + 200}
		val combinedArray = arrayA + arrayB
		assertContentEquals(arrayA, combinedArray - arrayB)
	}

	@Test
	fun `Array minus elements Test`()
	{
		val arrayA = Array(5){it}
		val arrayB = Array(4){it}
		assertContentEquals(arrayA - 4, arrayB)
	}
}
