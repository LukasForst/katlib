package dev.forst.katlib

/**
 * Applies [block] on left part of pair in List.
 */
inline fun <T, NT, V> Pair<Iterable<T>, V>.mapLeft(block: (T) -> NT): Pair<Iterable<NT>, V> =
    Pair(this.first.map(block), this.second)

/**
 * Applies [block] on right part of pair in List.
 */
inline fun <T, V, NV> Pair<T, Iterable<V>>.mapRight(block: (V) -> NV): Pair<T, Iterable<NV>> =
    Pair(this.first, this.second.map(block))

/**
 * Applies [leftBlock] on left part and [rightBlock] on right part.
 */
inline fun <T, V, NT, NV> Pair<Iterable<T>, Iterable<V>>.mapPair(
    leftBlock: (T) -> NT,
    rightBlock: (V) -> NV
): Pair<Iterable<NT>, Iterable<NV>> =
    Pair(this.first.map(leftBlock), this.second.map(rightBlock))

/**
 * Applies [block] on left part of pair.
 */
inline fun <T, NT, V> Pair<T, V>.letLeft(block: (T) -> NT): Pair<NT, V> =
    letPair(block) { it }

/**
 * Applies [block] on right part of pair.
 */
inline fun <T, V, NV> Pair<T, V>.letRight(block: (V) -> NV): Pair<T, NV> =
    letPair({ it }, block)

/**
 * Applies [leftBlock] on left part and [rightBlock] on right part.
 */
inline fun <T, V, NT, NV> Pair<T, V>.letPair(leftBlock: (T) -> NT, rightBlock: (V) -> NV): Pair<NT, NV> =
    Pair(leftBlock(this.first), rightBlock(this.second))
