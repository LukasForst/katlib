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

