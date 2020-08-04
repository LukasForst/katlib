package pw.forst.tools.katlib

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.util.UUID

class PureFunctionsKtTest {

    @Test
    fun `test isURL valid urls`() {
        val inputs = (0..5).map { "http://hello.com/$it" } + listOf("https://gooo.pw/a#b", "https://sm.ai:90")
        inputs.forEach { isURL(it).whenFalse { fail("Method did not recognize correctly $it! This is valid URL") } }
    }

    /**
     * Test with examples of urls that are false positives cases for the [isURL] function.
     */
    @Test
    fun isUrlFalsePositives() {
        val inputs = listOf("https://sm.ai,", "https://,", "https:/sm.ai/sd", "https:sm.ai/sd")
        inputs.forEach { isURL(it).whenFalse { fail("Method did recognize correctly $it, however it is expected to fail") } }
    }

    @Test
    fun `test isURL invalid urls`() {
        val inputs = (0..5).map { it.toString() } + listOf("gooo.pw", "ht://", "https//:sm.ai/sd", UUID.randomUUID().toString())
        inputs.forEach { isURL(it).whenTrue { fail("Method did not recognize correctly $it. This is not valid URL.") } }
    }

    @Test
    fun `test isUUID with valid inputs`() {
        val inputs = (0..5).map { UUID.randomUUID() } + UUID(0L, 0L)
        inputs.forEach { isUUID(it.toString()).whenFalse { fail("Method did recognize correctly $it, it is valid UUID.") } }
    }

    @Test
    fun `test isUUID with invalid inputs`() {
        val inputs = (0..5).map { it.toString() } + listOf("ajksdjkfjkgf", "", "sdfghfg", Long.toString())
        inputs.forEach { isUUID(it).whenTrue { fail("Method did recognize correctly $it, it is not valid UUID.") } }
    }
}
