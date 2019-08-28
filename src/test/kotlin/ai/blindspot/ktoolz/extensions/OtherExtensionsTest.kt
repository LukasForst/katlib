package ai.blindspot.ktoolz.extensions

import java.util.Optional
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

internal class OtherExtensionsTest {

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
}
