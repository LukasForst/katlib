package pw.forst.tools.katlib

import mu.KLogging
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.collections.LinkedHashSet
import kotlin.collections.component1
import kotlin.collections.component2

@PublishedApi
internal val iterableLogger = KLogging().logger("IterableExtensions")

/**
 * Function that will return a random element from the iterable.
 */
fun <E> Iterable<E>.getRandomElement(rand: Random) = this.elementAt(rand.nextInt(this.count()))

/**
 * Creates reduction of the given [Iterable]. This function can be used for example for cumulative sums.
 */
fun <T, R> Iterable<T>.reduction(initial: R, operation: (acc: R, T) -> R): List<R> {
    val result = ArrayList<R>()
    var last = initial
    for (item in this) {
        last = operation(last, item)
        result.add(last)
    }
    return result
}

/**
 * Returns the sum of all values produced by [selector] function applied to each element in the collection.
 */
inline fun <T> Iterable<T>.sumByLong(selector: (T) -> Long): Long {
    var sum: Long = 0
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

/**
 * Returns the sum of all values produced by [selector] function applied to each element in the sequence.
 *
 * The operation is _terminal_.
 */
inline fun <T> Sequence<T>.sumByLong(selector: (T) -> Long): Long {
    var sum = 0L
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

/**
 * Sums all Lists of integers into single one by indexes (i.e. all the numbers with the same index are always summed together).
 * If the lists have different lengths,
 * the final list has length corresponding to the shortest list in [this] iterable.
 */
fun Iterable<List<Int>>.sumByIndexes(): List<Int> {
    val minSize =
        this.minValueBy { it.size } ?: throw IllegalArgumentException("Only nonempty collections are supported.")
    val result = MutableList(minSize) { 0 }

    for (index in 0 until minSize) {
        for (list in this) {
            result[index] += list[index]
        }
    }
    return result
}

/**
 * Sums all Lists of integers into single one by indexes (i.e. all the numbers with the same index are always summed together). If the lists have different
 * lengths, the final list has length corresponding to the shortest list in [this] iterable.
 */
fun Iterable<List<Double>>.sumDoublesByIndexes(): List<Double> {
    val minSize =
        this.minValueBy { it.size } ?: throw IllegalArgumentException("Only nonempty collections are supported.")
    val result = MutableList(minSize) { 0.0 }

    for (index in 0 until minSize) {
        for (list in this) {
            result[index] += list[index]
        }
    }
    return result
}

/**
 * Returns the largest value of the given function or `null` if there are no elements.
 */
inline fun <T, R : Comparable<R>> Iterable<T>.maxValueBy(selector: (T) -> R): R? {
    val iterator = iterator()
    if (!iterator.hasNext()) return null
    var maxValue = selector(iterator.next())
    while (iterator.hasNext()) {
        val v = selector(iterator.next())
        if (maxValue < v) {
            maxValue = v
        }
    }
    return maxValue
}

/**
 * Returns the smallest value of the given function or `null` if there are no elements.
 */
inline fun <T, R : Comparable<R>> Iterable<T>.minValueBy(selector: (T) -> R): R? {
    val iterator = iterator()
    if (!iterator.hasNext()) return null
    var minValue = selector(iterator.next())
    while (iterator.hasNext()) {
        val v = selector(iterator.next())
        if (minValue > v) {
            minValue = v
        }
    }
    return minValue
}

/**
 * Applies the given [transform] function to each element of the original collection
 * and returns results as Set.
 */
inline fun <T, R> Iterable<T>.mapToSet(transform: (T) -> R): Set<R> {
    return mapTo(LinkedHashSet(), transform)
}

/**
 * Applies the given [transform] function to each element of the original collection
 * and returns results as Set.
 */
inline fun <T, R> Iterable<T>.flatMapToSet(transform: (T) -> Iterable<R>): Set<R> {
    return flatMapTo(LinkedHashSet(), transform)
}

/**
 * Returns the most frequently occurring value of the given function or `null` if there are no elements.
 */
fun <T, R> Iterable<T>.dominantValueBy(selector: (T) -> R): R? =
    this.groupingBy(selector).eachCount().maxBy { it.value }?.key

/**
 * Creates cartesian product between all the elements from [this] and [other] iterable. E.g. when [this] contains [1,2,3] and [other] contains ['a', 'b'], the
 * result will be {Pair(1,'a'), Pair(1,'b'), Pair(2,'a'), Pair(2,'b'), Pair(3,'a'), Pair(3,'b')}.
 */
fun <T1, T2> Iterable<T1>.cartesianProduct(other: Iterable<T2>): Set<Pair<T1, T2>> {
    val result = LinkedHashSet<Pair<T1, T2>>(this.count() * other.count())
    for (item1 in this) {
        for (item2 in other) {
            result.add(Pair(item1, item2))
        }
    }
    return result
}

/**
 * Performs the given [action] on each element that is not null.
 */
inline fun <T : Any> Iterable<T?>.forEachNotNull(action: (T) -> Unit) {
    for (element in this) element?.let(action)
}

/**
 * Creates union of the given iterables.
 */
fun <T> Iterable<Iterable<T>?>.union(): Set<T> {
    val result = LinkedHashSet<T>()
    this.forEachNotNull { input -> result.addAll(input) }
    return result
}

/**
 * Creates intersection of the given iterables.
 */
fun <T> Iterable<Iterable<T>?>.intersect(): Set<T> {
    val result = LinkedHashSet<T>()
    var first = true
    for (item in this) {
        if (item == null) continue
        if (first) {
            first = false
            result.addAll(item)
        } else result.retainAll(item)
    }
    return result
}

/**
 * Returns a list containing only the non-null results of applying the given transform function to each element in the original collection.
 */
fun <T : Any, R : Any> Iterable<T?>.filterNotNullBy(selector: (T) -> R?): List<T> {
    val result = ArrayList<T>()
    this.mapNotNull { }
    for (item in this) {
        if (item != null && selector(item) != null) result.add(item)
    }
    return result
}

/**
 * Returns the single element matching the given [predicate], or `null` if element was not found.
 *
 * Throws [IllegalArgumentException] when multiple elements are matching predicate.
 */
inline fun <T> Iterable<T>.singleOrEmpty(predicate: (T) -> Boolean): T? {
    var single: T? = null
    var found = false
    for (element in this) {
        if (predicate(element)) {
            if (found) throw IllegalArgumentException("Collection contains more than one matching element.")
            single = element
            found = true
        }
    }
    return single
}


/**
 * Returns single element, or `null` if the collection is empty.
 * Throws [IllegalArgumentException] when multiple elements are matching predicate.
 */
fun <T> Iterable<T>.singleOrEmpty(): T? {
    when (this) {
        is List -> return if (size == 0) null else if (size == 1) this[0] else throw IllegalArgumentException("Collection contains more than one element.")
        else -> {
            val iterator = iterator()
            if (!iterator.hasNext())
                return null
            val single = iterator.next()
            if (iterator.hasNext())
                throw IllegalArgumentException("Collection contains more than one element.")
            return single
        }
    }
}

/**
 * Takes Iterable with pairs and returns pair of collections filled with values in each part of pair.
 * */
fun <T, V> Iterable<Pair<T, V>>.splitPairCollection(): Pair<List<T>, List<V>> {
    val ts = mutableListOf<T>()
    val vs = mutableListOf<V>()
    for ((t, v) in this) {
        ts.add(t)
        vs.add(v)
    }
    return ts to vs
}

/**
 * Returns all values that are in [this] and not in [other] with custom [selector].
 * */
inline fun <T, R> Iterable<T>.setDifferenceBy(other: Iterable<T>, selector: (T) -> R): List<T> =
    (this.distinctBy(selector).map { Pair(it, true) } + other.distinctBy(selector).map { Pair(it, false) })
        .groupBy { selector(it.first) }
        .filterValues { it.size == 1 && it.single().second }
        .map { (_, value) -> value.single().first }


/**
 * Returns a [Map] containing key-value pairs provided by elements of the given collection.
 *
 * If any of two pairs would have the same key the last one gets added to the map and the method creates a warning.
 *
 * The returned map preserves the entry iteration order of the original collection.
 */
fun <K, V> Iterable<Pair<K, V>>.assoc(): Map<K, V> {
    return assocTo(LinkedHashMap(collectionSizeOrDefault(10)))
}

/**
 * Populates and returns the [destination] mutable map with key-value pairs
 * provided by elements of the given collection.
 *
 * If any of two pairs would have the same key the last one gets added to the map and the method creates a warning.
 */
fun <K, V, M : MutableMap<in K, in V>> Iterable<Pair<K, V>>.assocTo(destination: M): M {
    var size = 0
    for ((key, value) in this) {
        destination.put(key, value)
        size++
    }
    destination.checkUniqueness(size) { this.groupBy({ it.first }, { it.second }) }
    return destination
}

/**
 * Returns a [Map] containing key-value pairs provided by [transform] function
 * applied to elements of the given collection.
 *
 * If any of two pairs would have the same key the last one gets added to the map and the method creates a warning.
 *
 * The returned map preserves the entry iteration order of the original collection.
 */
inline fun <T, K, V> Iterable<T>.assoc(transform: (T) -> Pair<K, V>): Map<K, V> {
    val capacity = mapCapacity(collectionSizeOrDefault(10)).coerceAtLeast(16)
    return assocTo(LinkedHashMap(capacity), transform)
}


/**
 * Populates and returns the [destination] mutable map with key-value pairs
 * provided by [transform] function applied to each element of the given collection.
 *
 * If any of two pairs would have the same key the last one gets added to the map and the method creates a warning.
 */
inline fun <T, K, V, M : MutableMap<in K, in V>> Iterable<T>.assocTo(destination: M, transform: (T) -> Pair<K, V>): M {
    var size = 0
    for (element in this) {
        destination += transform(element)
        size++
    }
    destination.checkUniqueness(size) { this.groupBy({ transform(it).first }, { transform(it).second }) }
    return destination
}

/**
 * Returns a [Map] containing the elements from the given collection indexed by the key
 * returned from [keySelector] function applied to each element.
 *
 * If any two elements would have the same key returned by [keySelector] the last one gets added to the map and the method creates a warning.
 *
 * The returned map preserves the entry iteration order of the original collection.
 */
inline fun <T, K> Iterable<T>.assocBy(keySelector: (T) -> K): Map<K, T> {
    val capacity = mapCapacity(collectionSizeOrDefault(10)).coerceAtLeast(16)
    return assocByTo(LinkedHashMap(capacity), keySelector)
}

/**
 * Populates and returns the [destination] mutable map with key-value pairs,
 * where key is provided by the [keySelector] function applied to each element of the given collection
 * and value is the element itself.
 *
 * If any two elements would have the same key returned by [keySelector] the last one gets added to the map and the method creates a warning.
 */
inline fun <T, K, M : MutableMap<in K, in T>> Iterable<T>.assocByTo(destination: M, keySelector: (T) -> K): M {
    var size = 0
    for (element in this) {
        destination.put(keySelector(element), element)
        size++
    }
    destination.checkUniqueness(size) { this.groupBy(keySelector) }
    return destination
}

/**
 * Returns a [Map] containing the values provided by [valueTransform] and indexed by [keySelector] functions applied to elements of the given collection.
 *
 * If any two elements would have the same key returned by [keySelector] the last one gets added to the map and the method creates a warning.
 *
 * The returned map preserves the entry iteration order of the original collection.
 */
inline fun <T, K, V> Iterable<T>.assocBy(keySelector: (T) -> K, valueTransform: (T) -> V): Map<K, V> {
    val capacity = mapCapacity(collectionSizeOrDefault(10)).coerceAtLeast(16)
    return assocByTo(LinkedHashMap(capacity), keySelector, valueTransform)
}

/**
 * Populates and returns the [destination] mutable map with key-value pairs,
 * where key is provided by the [keySelector] function and
 * and value is provided by the [valueTransform] function applied to elements of the given collection.
 *
 * If any two elements would have the same key returned by [keySelector] the last one gets added to the map and the method creates a warning.
 */
inline fun <T, K, V, M : MutableMap<in K, in V>> Iterable<T>.assocByTo(
    destination: M,
    keySelector: (T) -> K,
    valueTransform: (T) -> V
): M {
    var size = 0
    for (element in this) {
        destination.put(keySelector(element), valueTransform(element))
        size++
    }
    destination.checkUniqueness(size) { this.groupBy(keySelector, valueTransform) }
    return destination
}


/**
 * Returns a [Map] where keys are elements from the given collection and values are
 * produced by the [valueSelector] function applied to each element.
 *
 * If any two elements are equal, the last one gets added to the map and the method creates a warning.
 *
 * The returned map preserves the entry iteration order of the original collection.
 *
 */
inline fun <K, V> Iterable<K>.assocWith(valueSelector: (K) -> V): Map<K, V> {
    val result = LinkedHashMap<K, V>(mapCapacity(collectionSizeOrDefault(10)).coerceAtLeast(16))
    return assocWithTo(result, valueSelector)
}

/**
 * Populates and returns the [destination] mutable map with key-value pairs for each element of the given collection,
 * where key is the element itself and value is provided by the [valueSelector] function applied to that key.
 *
 * If any two elements are equal, the last one overwrites the former value in the map and the method creates a warning.
 */
inline fun <K, V, M : MutableMap<in K, in V>> Iterable<K>.assocWithTo(destination: M, valueSelector: (K) -> V): M {
    var size = 0
    for (element in this) {
        destination.put(element, valueSelector(element))
        size++
    }
    destination.checkUniqueness(size) { this.groupBy({ it }, valueSelector) }
    return destination
}

/**
 * Checks that [this] map has [expectedSize] and if it is not the case (because value for some key was overwritten), warning with affected keys is generated.
 */
@PublishedApi
internal inline fun <K, V, M : MutableMap<in K, in V>> M.checkUniqueness(
    expectedSize: Int,
    grouping: () -> Map<K, List<V>>
) {
    if (this.size == expectedSize) return
    val duplicatedKeys = grouping().filterValues { it.size > 1 }
    iterableLogger.warn(Throwable()) {
        val entries =
            duplicatedKeys.entries.toString().take(500) //ensures that huge collections will not consume too much space
        "The map should contain $expectedSize entries but the actual size is ${this.size}. The affected entries are $entries."
    }

}

internal const val INT_MAX_POWER_OF_TWO: Int = Int.MAX_VALUE / 2 + 1

/**
 * Calculate the initial capacity of a map, based on Guava's com.google.common.collect.Maps approach. This is equivalent
 * to the Collection constructor for HashSet, (c.size()/.75f) + 1, but provides further optimisations for very small or
 * very large sizes, allows support non-collection classes, and provides consistency for all map based class construction.
 */
@PublishedApi
internal fun mapCapacity(expectedSize: Int): Int {
    if (expectedSize < 3) {
        return expectedSize + 1
    }
    if (expectedSize < INT_MAX_POWER_OF_TWO) {
        return expectedSize + expectedSize / 3
    }
    return Int.MAX_VALUE // any large value
}

/**
 * Returns the size of this iterable if it is known, or the specified [default] value otherwise.
 */
@PublishedApi
internal fun <T> Iterable<T>.collectionSizeOrDefault(default: Int): Int =
    if (this is Collection<*>) this.size else default

/**
 * Returns three lists with separated values from list of triples.
 * */
fun <A, B, C> Iterable<Triple<A, B, C>>.flattenToLists(): Triple<List<A>, List<B>, List<C>> {
    val aList = mutableListOf<A>()
    val bList = mutableListOf<B>()
    val cList = mutableListOf<C>()

    for ((a, b, c) in this) {
        aList.add(a)
        bList.add(b)
        cList.add(c)
    }
    return Triple(aList, bList, cList)
}

/**
 * Returns a [NavigableSet] of all elements.
 *
 * Elements in the set returned are sorted according to the given [comparator].
 */
fun <T> Iterable<T>.toNavigableSet(comparator: Comparator<in T>): NavigableSet<T> {
    return toCollection(TreeSet(comparator))
}

/**
 * Formats a collection of [TItem]s to a readable string like "3 colors: red, yellow, green".
 *
 * Each item is converted by [itemToString] to a string representation whose length is restricted by [itemLength]
 *  and the output length by [totalLength]. A [separator] (default = ", ") is placed between the string representation.
 *
 * @param itemsType a string describing the collection items, such as "colors", "employees" etc.; default = "items"
 * @param itemToString a lambda converting each item to its string representation.
 */
inline fun <TItem> Iterable<TItem>.itemsToString(
    itemsType: String = "items",
    separator: String = ", ",
    itemLength: Int = 30,
    totalLength: Int = 200,
    itemToString: (TItem) -> String = { item -> item.toShortString() }
): String {
    val sb = StringBuilder("${this.count()} $itemsType")
    var currentSeparator = ": "  // before the first item
    for (item in this) {
        sb.append(currentSeparator)
        currentSeparator = separator // before other than first item
        val short: String = if (item == null) "null" else itemToString(item).restrictLengthWithEllipsis(itemLength)
        if (short.length + sb.length > totalLength) {
            sb.append("…")
            break // no more items will fit into [totalLength]
        }
        sb.append(short)
    }
    return sb.toString().restrictLengthWithEllipsis(totalLength)
}

/**
 * Returns a single list of all not null elements yielded from results of [transform]
 * function being invoked on each element of original collection.
 */
fun <T, R> Iterable<T>.flatMapIndexedNotNull(transform: (index: Int, T) -> Iterable<R>?): List<R> {
    return flatMapIndexedTo(ArrayList(), transform)
}

inline fun <T, R, C : MutableCollection<in R>> Iterable<T>.flatMapIndexedTo(
    destination: C,
    transform: (index: Int, T) -> Iterable<R>?
): C {

    forEachIndexed { index, element ->
        transform(index, element)?.let { elements ->
            destination.addAll(elements)
        }
    }

    return destination
}

/**
 * Validates the relationship between every element of an Iterable.
 *
 * Iterates through the elements invoking the validationFunction on each one.
 * Returns false on the first element that does not pass the validation function, otherwise true.
 *
 * @param validationFunction is the accumulator function that verifies the elements.
 */
fun <S, T : S> Iterable<T>.foldValidated(validationFunction: (acc: S, T) -> Boolean): Boolean {
    val iterator = this.iterator()
    if (!iterator.hasNext()) return false

    var accumulator: T = iterator.next()
    var isValid = true

    while (iterator.hasNext()) {
        val element = iterator.next()
        val elementsAreValid = validationFunction(accumulator, element)

        if (elementsAreValid) {
            accumulator = element
        } else {
            isValid = false
            break
        }
    }

    return isValid
}
