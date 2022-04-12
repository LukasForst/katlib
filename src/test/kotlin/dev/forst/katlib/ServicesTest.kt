package dev.forst.katlib

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ServicesTest {
    @Test
    fun `test mock TimeProvider`() {
        val time = Instant.ofEpochSecond(1_000_000L)

        val timeProviderMock = mockk<TimeProvider<Instant>>()
        every { timeProviderMock.now() } returns time

        assertEquals(time, timeProviderMock.now())
    }

    @Test
    fun `test InstantTimeProvider`() {
        val provider: TimeProvider<Instant> = InstantTimeProvider

        val preNow = Instant.now().toEpochMilli()
        val providerNow = provider.now().toEpochMilli()
        val postNow = Instant.now().toEpochMilli()

        assertTrue { preNow <= providerNow }
        assertTrue { providerNow <= postNow }
    }
}
