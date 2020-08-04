package pw.forst.tools.katlib

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class StringExtensionsTest {

    @Test
    fun restrictLengthWithEllipsis() {
        val result = "ABCDEFGHIJK".restrictLengthWithEllipsis(8, "elip")
        assertEquals("ABCDelip", result)
    }

    @Test
    fun startsWithLetter(){
        val examples = mapOf("a lPha" to true, "Běta7" to true, "Čermák" to false, " delta" to false, "30" to false, "_" to false)
        for ( (key, value) in examples) {
            val result = key.startsWithLetter()
            assertEquals(value, result, "The call \"$key\".startsWithLetter() should return $value")
        }
    }
}
