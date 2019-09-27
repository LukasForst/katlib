package ai.blindspot.ktoolz.extensions

import java.time.Instant
import kotlin.math.abs

/**
 * Computes duration in milliseconds between two [Instant] instances.
 * */
fun Instant.durationToInMilli(other: Instant): Long = durationInMilli(this, other)

/**
 * Computes duration in milliseconds between two [Instant] instances.
 * */
fun durationInMilli(a: Instant, b: Instant): Long = abs(a.toEpochMilli() - b.toEpochMilli())
