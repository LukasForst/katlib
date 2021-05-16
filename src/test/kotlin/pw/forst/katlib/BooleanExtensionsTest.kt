package pw.forst.katlib

import org.junit.jupiter.api.Test
import kotlin.test.fail

internal class BooleanExtensionsTest {
    @Test
    fun `test whenTrue executed`() {
        true.whenTrue { return }
        fail()
    }

    @Test
    fun `test whenTrue not executed`() {
        false.whenTrue { fail() }
    }

    @Test
    fun `test whenFalse executed`() {
        false.whenFalse { return }
        fail()
    }

    @Test
    fun `test whenFalse not executed`() {
        true.whenFalse { fail() }
    }
}
