package pw.forst.tools.katlib

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


internal class SystemExtensionsTest {

    @Test
    fun `getEnv returns same value as System getenv`() {
        val wellKnownExistingEnvVariable = "PATH"
        assertEquals(getEnv(wellKnownExistingEnvVariable), System.getenv(wellKnownExistingEnvVariable))
    }

    @Test
    fun `test newLine returns System lineSeparator`() {
        assertEquals(System.lineSeparator(), newLine)
    }
}
