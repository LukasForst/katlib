package pw.forst.katlib

import org.junit.jupiter.api.Test
import java.nio.ByteBuffer
import java.util.Optional
import java.util.UUID
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
        val notNull = 1
        assertEquals(
            1,
            notNull.whenNull { fail("Value is not null, this thing should not be called") },
            "Original value should be returned."
        )

        val nonNullable = 1
        assertEquals(
            1,
            nonNullable.whenNull { fail("Value is not null, this thing should not be called") },
            "Original value should be returned."
        )

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
    fun toLongString() {
        // With default parameters
        assertEquals("Double(42.0)", 42.0.toLongString("42.0"))
        // With explicit parameters
        assertEquals("MyClass[short]", "Any object".toLongString("short", "[]", className = "MyClass"))
    }

    @Test
    fun toShortString() {
        val short = "SHORT"
        val long = short.toLongString(short)
        assertEquals(short, long.toShortString())
    }

    @Test
    fun `test isURL valid urls`() {
        val inputs = (0..5).map { "http://hello.com/$it" } + listOf("https://gooo.pw/a#b", "https://sm.ai:90")
        inputs.forEach { isUrl(it).whenFalse { fail("Method did not recognize correctly $it! This is valid URL") } }
    }

    /**
     * Test with examples of urls that are false positives cases for the [isURL] function.
     */
    @Test
    fun isUrlFalsePositives() {
        val inputs = listOf("https://sm.ai,", "https://,", "https:/sm.ai/sd", "https:sm.ai/sd")
        inputs.forEach {
            isUrl(it).whenFalse {
                fail("Method did recognize correctly $it, however it is expected to fail")
            }
        }
    }

    @Test
    fun `test isURL invalid urls`() {
        val inputs = (0..5).map { it.toString() } + listOf("gooo.pw", "ht://", "https//:sm.ai/sd", UUID.randomUUID().toString())
        inputs.forEach { isUrl(it).whenTrue { fail("Method did not recognize correctly $it. This is not valid URL.") } }
    }

    @Test
    fun `test isUUID with valid inputs`() {
        val inputs = (0..5).map { UUID.randomUUID() } + UUID(0L, 0L)
        inputs.forEach { isUuid(it.toString()).whenFalse { fail("Method did recognize correctly $it, it is valid UUID.") } }
    }

    @Test
    fun `test isUUID with invalid inputs`() {
        val inputs = (0..5).map { it.toString() } + listOf("ajksdjkfjkgf", "", "sdfghfg", Long.toString())
        inputs.forEach { isUuid(it).whenTrue { fail("Method did recognize correctly $it, it is not valid UUID.") } }
    }

    @Test
    fun `getEnv returns same value as System getenv`() {
        val wellKnownExistingEnvVariable = "PATH"
        assertEquals(getEnv(wellKnownExistingEnvVariable), System.getenv(wellKnownExistingEnvVariable))
    }

    @Test
    fun `test newLine returns System lineSeparator`() {
        assertEquals(System.lineSeparator(), newLine)
    }

    @Test
    fun `test load toplevel props`() {
        val props = propertiesFromResources("/toplevel.properties")
        assertNotNull(props)
        assertEquals("yes", props.getProperty("loaded"))
    }

    @Test
    fun `test load classLevel props`() {
        val props = propertiesFromResources("classlevel.properties")
        assertNotNull(props)
        assertEquals("yes", props.getProperty("loaded"))
    }

    @Test
    fun `test toUuid`() {
        (0..10).map { UUID.randomUUID() }
            .map { uuid ->
                val array = ByteBuffer.allocate(Long.SIZE_BYTES * 2).apply {
                    putLong(uuid.mostSignificantBits)
                    putLong(uuid.leastSignificantBits)
                }.array()

                assertEquals(uuid, array.toUuid())
            }
    }

    @Test
    fun `test toUuidFlipped`() {
        (0..10).map { UUID.randomUUID() }
            .map { uuid ->
                val array = ByteBuffer.allocate(Long.SIZE_BYTES * 2).apply {
                    putLong(uuid.leastSignificantBits)
                    putLong(uuid.mostSignificantBits)
                }.array()

                assertEquals(uuid, array.toUuidFlipped())
            }
    }

    @Test
    fun `test applyIfNotNull`() {
        val something = Any()

        something.applyIfNotNull(null) { fail("This block should never be executed as the supplied value is null!") }

        val someValue = Any()
        var wasExecuted = false
        something.applyIfNotNull(someValue) {
            assertSame(something, this)
            assertSame(someValue, it)
            wasExecuted = true
        }
        assertTrue(wasExecuted)
    }
}
