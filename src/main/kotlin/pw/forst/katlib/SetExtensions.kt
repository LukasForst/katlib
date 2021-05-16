package pw.forst.katlib

import java.util.SortedSet


/**
 * Returns the first item in this sorted set.
 */
fun <E> SortedSet<E>.min(): E? = this.firstOrNull()

/**
 * Returns the last item in this sorted set.
 */
fun <E> SortedSet<E>.max(): E? = this.lastOrNull()
