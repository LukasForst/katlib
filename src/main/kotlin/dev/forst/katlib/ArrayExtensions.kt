package dev.forst.katlib

import kotlin.experimental.ExperimentalTypeInference

/**
 * Builds a new [Array] by populating a [MutableList] using the given [builderAction]
 * and returning an Array with the same elements.
 *
 * The list passed as a receiver to the [builderAction] is valid only inside that function.
 * Using it outside of the function produces an unspecified behavior.
 */
/* 	It uses BuilderInference just like buildList,
	but it can't use buildLists @WasExperimental(ExperimentalStdlibApi::class) annotation because it's kotlin internal*/
@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
@OptIn(ExperimentalTypeInference::class)
inline fun <reified E> buildArray(@BuilderInference builderAction: MutableList<E>.() -> Unit) = buildList(builderAction).toTypedArray()

/**
 * Returns an array containing the results of applying the given [transform] function
 * to each element in the original array.
 */
inline fun <T, reified R> Array<out T>.map(transform: (T) -> R) = mapTo(ArrayList(size), transform).toTypedArray()

/**
 * Returns an array containing the results of applying the given [transform] function
 * to each element and its index in the original array.
 * @param [transform] function that takes the index of an element and the element itself
 * and returns the result of the transform applied to the element.
 */
inline fun <T, reified R> Array<out T>.mapIndexed(transform: (index: Int, T) -> R) = mapIndexedTo(ArrayList(size), transform).toTypedArray()

/**
 * Returns an array containing only elements matching the given [predicate].
 */
inline fun <reified T> Array<out T>.filter(predicate: (T) -> Boolean) = filterTo(ArrayList(), predicate).toTypedArray()

/**
 * Returns an array containing all elements not matching the given [predicate].
 */
inline fun <reified T> Array<out T>.filterNot(predicate: (T) -> Boolean) = filterNotTo(ArrayList(), predicate).toTypedArray()

/**
 * Returns an array containing only elements matching the given [predicate].
 * @param [predicate] function that takes the index of an element and the element itself
 * and returns the result of predicate evaluation on the element.
 */
inline fun <reified T> Array<out T>.filterIndexed(predicate: (index: Int, T) -> Boolean) =
	filterIndexedTo(ArrayList(), predicate).toTypedArray()

/**
 * Returns an array containing all elements that are instances of specified type parameter R.
 */
inline fun <reified R> Array<*>.filterIsInstance() = filterIsInstanceTo(ArrayList<R>()).toTypedArray()

/**
 * Returns an array containing all elements that are not `null`.
 */
inline fun <reified T : Any> Array<out T?>.filterNotNull() = filterNotNullTo(ArrayList<T>()).toTypedArray()

/**
 * Returns an array containing all elements of the original collection without the first occurrence of the given [element].
 */
inline operator fun <reified T> Array<out T>.minus(element: T) = asList<T>().minus(element).toTypedArray()

/**
 * Returns an array containing all elements of the original collection except the elements contained in the given [elements] array.
 */
@Suppress("ConvertArgumentToSet")
inline operator fun <reified T> Array<out T>.minus(elements: Array<out T>) = asList<T>().minus(elements).toTypedArray()
