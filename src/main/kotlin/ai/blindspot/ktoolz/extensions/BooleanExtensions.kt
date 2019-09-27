package ai.blindspot.ktoolz.extensions

/**
 * Executes [block] iff this (result of previous method) is true. Returns given Boolean.
 * */
inline fun Boolean.whenTrue(block: () -> Unit): Boolean {
    if (this) block()
    return this
}

/**
 * Executes [block] iff this (result of previous method) is false. Returns given Boolean.
 * */
inline fun Boolean.whenFalse(block: () -> Unit): Boolean {
    if (!this) block()
    return this
}
