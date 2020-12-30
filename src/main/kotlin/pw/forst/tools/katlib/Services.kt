package pw.forst.tools.katlib

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.Temporal

/**
 * Interface providing access to current time via [now] method.
 *
 * This class is needed in order to test methods that require usage of current time stamp.
 */
interface TimeProvider<T : Temporal> {

    /**
     * Returns value representing current time stamp as defined in the implementation of [T].
     */
    fun now(): T
}

/**
 *  Implementation of [TimeProvider] providing access to [Instant.now] method.
 */
object InstantTimeProvider : TimeProvider<Instant> {

    override fun now(): Instant = OffsetDateTime.now(ZoneOffset.UTC).toInstant()
}
