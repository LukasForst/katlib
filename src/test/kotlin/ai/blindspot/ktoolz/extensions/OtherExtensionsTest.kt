package ai.blindspot.ktoolz.extensions

import org.junit.jupiter.api.Test
import java.util.Optional
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlin.test.fail

internal class OtherExtensionsTest {

    @Test
    fun kClassTest() {
        assertEquals(String::class, kClass())
        assertEquals(List::class, kClass())

        // it is not possible to test other cases since generics are always erased in the runtime
    }

    @Test
    fun propagateNullTest() {
        val allNullPair: Pair<String?, Int?> = null to null
        assertNull(allNullPair.propagateNull())

        val leftNullPair: Pair<String?, Int?> = null to 0
        assertNull(leftNullPair.propagateNull())

        val rightNullPair: Pair<String?, Int?> = "null" to null
        assertNull(rightNullPair.propagateNull())

        val notNullPair: Pair<String?, Int?> = "null" to 0
        val actual = notNullPair.propagateNull()

        assertNotNull(actual)
        assertEquals(notNullPair, actual)
    }

    @Test
    fun orNullWithValue() {
        val storedValue = 1.0
        val optional = Optional.of(storedValue)
        assertEquals(storedValue, optional.orNull())
    }

    @Test
    fun orNullWithNull() {
        val optional = Optional.empty<Int>()
        assertNull(optional.orNull())
    }

    @Test
    fun testWhenNull() {
        val notNull: Int? = 1
        assertEquals(1, notNull.whenNull { fail("Value is not null, this thing should not be called") }, "Original value should be returned.")

        val nonNullable = 1
        assertEquals(1, nonNullable.whenNull { fail("Value is not null, this thing should not be called") }, "Original value should be returned.")

        var response = false
        val value: Int? = null
        assertNull(value.whenNull { response = true })
        assertTrue(response)
    }

    @Test
    fun testAsList() {
        assertEquals(listOf(1), 1.asList())
        val nullable: Int? = null
        assertNull(nullable?.asList())
    }

    @Test
    fun testIntersect() {
        val range1 = 1..5
        val range2 = 5..7
        val range3 = 0..1
        val range4 = 6..7
        val range5 = 0..0

        assertTrue { range1.intersects(range2) }
        assertTrue { range1.intersects(range3) }

        assertFalse { range1.intersects(range4) }
        assertFalse { range1.intersects(range5) }
    }

    @Test
    fun `with test`() {
        val a = 1
        val b = 3
        val result = a with b
        assertEquals(2, result.size)

        assertEquals(a, result[0])
        assertEquals(b, result[1])
    }

    @Test
    fun `validate test`() {
        val txt = "hello world"
        assertFails {
            txt.validate(false) { fail() }
        }

        assertFails {
            txt.validate(isValidSelector = { it == "$txt wrong!" }, invalidBlock = { fail() })
        }

        txt.validate(true) { fail() }
        txt.validate(isValidSelector = { it == txt }, invalidBlock = { fail() })
    }

    @Test
    fun `test applyIf applied`() {
        val listUnderTest = mutableListOf(1)

        assertSame(listUnderTest, listUnderTest.applyIf(true) { set(0, 0) })
        assertEquals(0, listUnderTest[0])
    }

    @Test
    fun `test applyIf not applied()`() {
        val listUnderTest = mutableListOf(1)

        assertSame(listUnderTest, listUnderTest.applyIf(false) { set(0, 0) })
        assertEquals(1, listUnderTest[0])
    }

    @Test
    fun `test applyIf via block applied`() {
        val listUnderTest = mutableListOf(1)

        assertSame(listUnderTest, listUnderTest.applyIf(shouldApplyBlock = {
            assertSame(listUnderTest, it)
            true
        }, block = { set(0, 0) }))
        assertEquals(0, listUnderTest[0])
    }

    @Test
    fun `test applyIf via block not applied()`() {
        val listUnderTest = mutableListOf(1)

        assertSame(listUnderTest, listUnderTest.applyIf(shouldApplyBlock = {
            assertSame(listUnderTest, it)
            false
        }, block = { set(0, 0) }))
        assertEquals(1, listUnderTest[0])
    }

    @Test
    fun restrictLengthWithEllipsis() {
        val result = "ABCDEFGHIJK".restrictLengthWithEllipsis(8, "elip")
        assertEquals("ABCDelip", result)
    }

    @Test
    fun toLongDebugString() {
        assertEquals("Double(42.0)", 42.0.toLongDebugString())
        assertEquals("MyClass[short]", "Any object".toLongDebugString("short","[]", className = "MyClass"))
    }

    @Test
    fun toShortDebugString() {
        val short = "SHORT"
        val long = short.toLongDebugString()
        assertEquals( short, long.toShortDebugString())
    }

    @Test
    fun itemsToDebugString() {
        val itemToString : (Int)->String = {i -> "NUM$i"}
        val result = setOf(10, 20).itemsToDebugString("numbers", itemToString = itemToString )
        assertEquals("2 numbers: NUM10, NUM20", result)
    }
}
