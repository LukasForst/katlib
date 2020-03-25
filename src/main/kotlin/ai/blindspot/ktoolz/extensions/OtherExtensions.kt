package ai.blindspot.ktoolz.extensions

import java.util.Optional
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
inline fun <T : Any> T.applyIf(shouldApplyBlock: (T) -> Boolean, block: T.() -> Unit): T = applyIf(shouldApplyBlock(this), block)

/**
 * Calls the specified function [block] with `this` value as its receiver if and only if the [shouldApply] parameter is true.
 * Returns `this` value.
 */
inline fun <T : Any> T.applyIf(shouldApply: Boolean, block: T.() -> Unit): T {
    if (shouldApply) block()
    return this
}

/**
 * Shortens the string if needed. For example, returns "ABCD…" when called "ABCDEFHG.(5, "…")
 */
fun String.restrictLengthWithEllipsis(maxLength: Int, ellipsis: String = "…"): String {
    if (this.length <= maxLength) return this
    return this.substring(0, maxLength - ellipsis.length) + ellipsis
}

/**
 * Creates a string like "className(details)", for example "Double(42.0)"
 * Useful for implementing .toString() method (but in such case the first parameter MUST be passed).
 */
fun Any.toLongDebugString(description: String? = null, brackets: String = "()", className: String? = null): String {
    val actualDescription = description ?: this.toString()
    val actualClassName = className ?: this.javaClass.simpleName
    val actualBrackets = if (brackets.length == 2) brackets else "<>"
    return "$actualClassName${actualBrackets[0]}$actualDescription${actualBrackets[1]}"
}

/**
 * Extracts the essential information from an object (most usually a string), whose toString() call result
 * has a similar format as the output of function [toLongDebugString] .
 * If the format is not similar, it simply returns this.toString() .
 *
 * For example, returns "42.0" for string "Double(42.0)".
 * Returns "John 42, Peter 31" for string "EmployeeList{John 42, Peter 31}"
 *
 * Typical usage: the toString() method of a large collection-like object should contain only a very short description
 *  of each collection item. This is done, for example, in [itemsToDebugString] ().
 */
fun Any?.toShortDebugString(): String {
    val longString = this.toString()
    if (longString.isEmpty()) return "EMPTY STRING" // Should not happen
    val startsWithLetter = longString[0].let { it in 'a'..'z' || it in 'A'..'Z' }
    if (!startsWithLetter) return longString // The format does not resemble an output of [toLongDebugString]

    val bracketPairs = setOf("()", "[]", "<>", "{}")
    for (pair in bracketPairs) {
        if (longString.last() == pair[1]) {
            val after = longString.substringAfter(pair[0])
            if (after.length < 2) return longString
            return after.substring(0, after.length - 1) // Omit the last char
        }
    }
    return longString // The format does not resemble an output of [toLongDebugString]
}

/**
 * Formats a collection to a readable string like "3 colors: red, yellow, green".
 */
inline fun <T> Collection<T>.itemsToDebugString(
    itemsType: String = "items", // or "employees" etc.
    separator: String = ", ", // inserted between (string representations of) collection items
    itemLength: Int = 30, // max length
    totalLength: Int = 200,
    itemToString: (T) -> String = { item -> item.toShortDebugString() }
): String {
    val sb = StringBuilder("${this.size} $itemsType")
    var currentSeparator = ": "  // we want to return something like: "3 items: item1, item2, item3"
    for (item in this) {
        val short: String = if (item == null) "NULL" else itemToString(item).restrictLengthWithEllipsis(itemLength)
        if (short.length + sb.length >= totalLength) return sb.apply { append("…") }.toString()
        sb.append(currentSeparator + short)
        currentSeparator = separator
    }
    return sb.toString()
}