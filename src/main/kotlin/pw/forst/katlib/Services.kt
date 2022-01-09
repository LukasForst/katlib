package pw.forst.katlib

import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.Temporal

/**
 * Interface providing access to current time via [now] method.
 *
 * This class is needed in order to test methods that require usage of current time stamp.
 */
fun interface TimeProvider<T : Temporal> {

    /**
     * Returns value representing current time stamp as defined in the implementation of [T].
     */
    fun now(): T
}

/**
 * This name makes a bit more sense as we're constraining to temporal.
 */
typealias TemporalProvider<T> = TimeProvider<T>

/**
 * Implementation of [TimeProvider] providing access to [Instant.now] method.
 */
object InstantTimeProvider : TemporalProvider<Instant> {

    override fun now(): Instant = OffsetDateTime.now(ZoneOffset.UTC).toInstant()
}

/**
 * Shorter name for [InstantTimeProvider].
 */
typealias InstantProvider = InstantTimeProvider

/**
 * Implementation of [TimeProvider] providing access to [LocalDateTime.now] method.
 */
object LocalDateTimeProvider : TemporalProvider<LocalDateTime> {

    override fun now(): LocalDateTime = LocalDateTime.now()
}
