package ai.blindspot.ktoolz.extensions

/**
 * Retrieves environment variable from the system.
 */
fun getEnv(variableName: String): String? = System.getenv(variableName)

/**
 * Shortcut for [System.lineSeparator].
 */
val newLine: String get() = System.lineSeparator()
