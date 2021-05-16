package pw.forst.katlib

import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class StringExtensionsTest {

    @Test
    fun `test restrictLengthWithEllipsis`() {
        val result = "ABCDEFGHIJK".restrictLengthWithEllipsis(8, "elip")
        assertEquals("ABCDelip", result)
    }

    @Test
    fun `test startsWithLetter`() {
        val examples = mapOf(
            "a lPha" to true, "Běta7" to true, "Čermák" to false,
            " delta" to false, "30" to false, "_" to false
        )
        for ((key, value) in examples) {
            val result = key.startsWithLetter()
            assertEquals(value, result, "The call \"$key\".startsWithLetter() should return $value")
        }
    }

    @Test
    fun `test toUuid`() {
        (0..10).map { UUID.randomUUID() }.map { uuid ->
            assertEquals(uuid, uuid.toString().toUuid())
        }
    }

    @Test
    fun `should test valid email address`() {
        assertTrue("testee@example.org".isEmail())
        assertTrue("testee+me1@example.org".isEmail())
        assertTrue("testee.me1@example.org".isEmail())
        assertTrue("testee@example.co.uk".isEmail())

        assertFalse("@example.org".isEmail())
        assertFalse("example.org".isEmail())
        assertFalse("example@org".isEmail())
        assertFalse("example".isEmail())
        assertFalse("".isEmail())
    }

}
