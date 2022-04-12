package dev.forst.katlib

import java.io.PrintWriter
import java.io.StringWriter
import java.net.URL
import java.nio.ByteBuffer
import java.util.Optional
import java.util.Properties
import java.util.UUID
import kotlin.reflect.KClass

/**
 * Returns value or null from Optional. Useful when using kotlin-like T? and Optional<T>.
 * */
fun <T> Optional<T>.orNull(): T? = this.orElse(null)

/**
 * Calls the specified function [block] only when `this` value is null and then returns `this` value.
 */
inline fun <T : Any?> T.whenNull(block: () -> Unit): T {
    if (this == null) block()
    return this
}


/**
 * Creates a single element list from [this].
 */
fun <T : Any> T.asList() = listOf(this)


/**
 * Returns true when there is at least one element x for which *x in this* and *x in other* returns true.
 */
fun <T : Comparable<T>> ClosedRange<T>.intersects(other: ClosedRange<T>): Boolean {
    return this.endInclusive >= other.start && this.start <= other.endInclusive
}

/**
 * Creates collection of [this] and other.
 * */
infix fun <T : Any> T.with(other: T): List<T> = listOf(this, other)

/**
 * If [isValid] is true, executes [invalidBlock]. Used mainly for validating entities -> do something when validation failed.
 * */
inline fun <T> T.validate(isValid: Boolean, invalidBlock: (T) -> Unit): T {
    if (!isValid) invalidBlock(this)
    return this
}

/**
 * If [isValidSelector] returns true, executes [invalidBlock]. Used mainly for validating entities -> do something when validation failed.
 * */
inline fun <T> T.validate(isValidSelector: (T) -> Boolean, invalidBlock: (T) -> Unit): T = validate(isValidSelector(this), invalidBlock)

/**
 * When both items in pair are not null, returns non-nullable pair, otherwise returns null.
 */
fun <A : Any, B : Any> Pair<A?, B?>.propagateNull(): Pair<A, B>? {
    if (first != null && second != null) {
        @Suppress("UNCHECKED_CAST") //the cast is safe because it is checked that both values are not null
        return this as Pair<A, B>
    }

    return null
}

/**
 * A function used to get the instance of class. This method allows to get class instances even for generic classes,
 * which cannot be obtained using :: notation.
 *
 * Usage example:
 * *kClass< List< String > >()* which returns *KClass< List< String > >*.
 *
 * Note that *List::class* returns *KClass< List< * > >* and *List< String >::class*
 * is not allowed.
 */
inline fun <reified T : Any> kClass(): KClass<T> = T::class

/**
 * Calls the specified function [block] with `this` value as its receiver if and only if the [shouldApplyBlock] lambda returns true.
 * Returns `this` value.
 */
inline fun <T : Any> T.applyIf(shouldApplyBlock: (T) -> Boolean, block: T.() -> Unit): T = applyIf(
    shouldApply = shouldApplyBlock(this),
    block = block
)

/**
 * Calls the specified function [block] with `this` value as its receiver if and only if the [shouldApply] parameter is true.
 * Returns `this` value.
 */
inline fun <T : Any> T.applyIf(shouldApply: Boolean, block: T.() -> Unit): T {
    if (shouldApply) block()
    return this
}

/**
 * Applies given [block] if the [value] is not null, supplies non-nullable value to the block as
 * an input.
 */
inline fun <T : Any, V : Any> T.applyIfNotNull(value: V?, block: T.(V) -> Unit): T {
    if (value != null) block(value)
    return this
}

/**
 * Creates a string like "className(description)", for example "Double(42.0)"
 * Useful e.g. for implementing .toString() override.
 *
 * @param description the content to be displayed inside the brackets.
 * @param brackets a two-character string like "{}", default = "()".
 * @param className the string to be displayed before the brackets; default = the class name of [this].
 */
fun Any.toLongString(description: String, brackets: String = "()", className: String? = null): String {
    val actualClassName = className ?: this.javaClass.simpleName
    val actualBrackets = if (brackets.length == 2) brackets else "<>"
    return "$actualClassName${actualBrackets[0]}$description${actualBrackets[1]}"
}

private val bracketPairs = setOf("()", "[]", "<>", "{}")

/**
 * Extracts the essential information from an object (most usually a string), whose toString() call result
 * has a similar format as the output of function [toLongString] .
 * If the format is not similar, it simply returns this.toString() .
 *
 * For example, returns "42.0" if [this] is a string "Double(42.0)".
 * Returns "John 42, Peter 31" for string "EmployeeList{John 42, Peter 31}".
 *
 * Typical usage: the toString() method of a large collection-like object should contain only a very short description
 *  of each collection item. This is done, for example, in [itemsToString] ().
 */
fun Any?.toShortString(): String {
    val longString = this.toString()
    return when {
        longString.isEmpty() -> "EMPTY STRING" // Should not happen
        !longString.startsWithLetter() -> longString // The format does not resemble an output of [toLongString]
        else -> {
            bracketPairs.firstOrNull { pair -> longString.last() == pair[1] }
                ?.let { pair ->
                    val after = longString.substringAfter(pair[0])
                    if (after.length < 2) {
                        longString
                    } else {
                        after.substring(0, after.length - 1) // Omit the last char
                    }
                } ?: longString // The format does not resemble an output of [toLongString]
        }
    }
}

/**
 * See documentation for [isUuid].
 */
@Deprecated("It is better to use Uuid instead of UUID.", replaceWith = ReplaceWith("isUuid(candidateUuid)"))
fun isUUID(candidateUuid: String): Boolean = isUuid(candidateUuid)

/**
 * Check whether a given string is a valid UUID.
 *
 * @param candidateUuid A candidate UUID to be checked.
 * @return true iff [candidateUuid] is a valid UUID.
 */
fun isUuid(candidateUuid: String): Boolean = runCatching { UUID.fromString(candidateUuid) }.isSuccess

/**
 * See documentation for [isUrl].
 */
@Deprecated("It is better to use Url instead of URL.", replaceWith = ReplaceWith("isUrl(candidateUrl)"))
fun isURL(candidateUrl: String): Boolean = isUrl(candidateUrl)

/**
 * Check whether a given string is a valid URL.
 *
 * Please note that this function is not all mighty and it only tries to convert the given string to URL and then to URI.
 * This means that it fails to recognize invalid urls such as  `https://sm.ai,` or `https://`.
 * For more complex validation, one should probably use Apache URL Validator.
 *
 * For the sample cases when this simple method fails please see `isUrlFalsePositives` test in the file OtherExtensionsTest.kt.
 *
 * @param candidateUrl A candidate URL to be checked.
 * @return true iff [candidateUrl] is a valid URL.
 */
fun isUrl(candidateUrl: String): Boolean = runCatching { URL(candidateUrl).toURI() }.isSuccess

/**
 * Retrieves environment variable from the system.
 */
fun getEnv(variableName: String): String? = System.getenv(variableName)

/**
 * Retrieves environment variable from the system, if the value is not found, [recoverBlock] is executed.
 */
inline fun getEnv(variableName: String, recoverBlock: () -> String): String = System.getenv(variableName) ?: recoverBlock()

/**
 * Shortcut for [System.lineSeparator].
 */
val newLine: String get() = System.lineSeparator()

/**
 * Loads properties file from the resources.
 * Returns null if the properties were not loaded.
 */
inline fun <reified T : Any> T.propertiesFromResources(resourcesPath: String): Properties? =
    javaClass.getResourceAsStream(resourcesPath)?.let { Properties().apply { load(it) } }

/**
 * Read [ByteArray] as two longs and combine the to UUID.
 *
 * Expects following order: [UUID.mostSigBits] and then [UUID.leastSigBits].
 */
fun ByteArray.toUuid(): UUID {
    require(size == Long.SIZE_BYTES * 2) {
        "Expected only two longs in the array! Expected size: ${Long.SIZE_BYTES * 2} but was $size"
    }

    return ByteBuffer.wrap(this).let {
        UUID(it.long, it.long)
    }
}

/**
 * Read [ByteArray] as two longs and combine the to UUID.
 *
 * Expects following order: [UUID.leastSigBits] - [UUID.mostSigBits].
 */
fun ByteArray.toUuidFlipped(): UUID {
    require(size == Long.SIZE_BYTES * 2) {
        "Expected only two longs in the array! Expected size: ${Long.SIZE_BYTES * 2} but was $size"
    }

    return ByteBuffer.wrap(this).let {
        val leastSignificant = it.long
        val mostSignificant = it.long
        UUID(mostSignificant, leastSignificant)
    }
}

/**
 * Converts stacktrace to the string. Uses [Throwable.printStackTrace] and returns it as string.
 */
@Deprecated(
    "Use standard kotlin implementation stackTraceToString available since kotlin 1.4.",
    replaceWith = ReplaceWith("stackTraceToString()")
)
fun Throwable.stacktraceToString(): String {
    val sw = StringWriter()
    val pw = PrintWriter(sw)
    printStackTrace(pw)
    pw.flush()
    return sw.toString()
}
