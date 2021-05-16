package pw.forst.katlib


/**
 * Default values taken from Guava.
 */
@PublishedApi
internal const val DEFAULT_COLLECTION_SIZE = 10

@PublishedApi
internal const val DEFAULT_COERCE_MINIMUM_VALUE = 16

/**
 * Returns a [Map] containing key-value pairs provided by elements of the given collection.
 *
 * If any of two pairs would have the same key the last one gets added to the map and the method creates a warning.
 *
 * The returned map preserves the entry iteration order of the original collection.
 */
fun <K, V> Sequence<Pair<K, V>>.assoc(): Map<K, V> =
    assocTo(LinkedHashMap(collectionSizeOrDefault(DEFAULT_COLLECTION_SIZE)))

/**
 * Populates and returns the [destination] mutable map with key-value pairs
 * provided by elements of the given collection.
 *
 * If any of two pairs would have the same key the last one gets added to the map and the method creates a warning.
 */
fun <K, V, M : MutableMap<in K, in V>> Sequence<Pair<K, V>>.assocTo(destination: M): M {
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
inline fun <T, K, V> Sequence<T>.assoc(transform: (T) -> Pair<K, V>): Map<K, V> =
    assocTo(LinkedHashMap(defaultMapCapacity()), transform)

/**
 * Returns a [Map] containing the elements from the given collection indexed by the key
 * returned from [keySelector] function applied to each element.
 *
 * If any two elements would have the same key returned by [keySelector] the last one gets added to the map and the method creates a warning.
 *
 * The returned map preserves the entry iteration order of the original collection.
 */
inline fun <T, K> Sequence<T>.assocBy(keySelector: (T) -> K): Map<K, T> =
    assocByTo(LinkedHashMap(defaultMapCapacity()), keySelector)

/**
 * Returns a [Map] containing the values provided by [valueTransform] and indexed by [keySelector] functions applied to elements of the given collection.
 *
 * If any two elements would have the same key returned by [keySelector] the last one gets added to the map and the method creates a warning.
 *
 * The returned map preserves the entry iteration order of the original collection.
 */
inline fun <T, K, V> Sequence<T>.assocBy(keySelector: (T) -> K, valueTransform: (T) -> V): Map<K, V> =
    assocByTo(LinkedHashMap(defaultMapCapacity()), keySelector, valueTransform)

/**
 * Populates and returns the [destination] mutable map with key-value pairs,
 * where key is provided by the [keySelector] function applied to each element of the given collection
 * and value is the element itself.
 *
 * If any two elements would have the same key returned by [keySelector] the last one gets added to the map and the method creates a warning.
 */
inline fun <T, K, M : MutableMap<in K, in T>> Sequence<T>.assocByTo(destination: M, keySelector: (T) -> K): M {
    var size = 0
    for (element in this) {
        destination.put(keySelector(element), element)
        size++
    }
    destination.checkUniqueness(size) { this.groupBy(keySelector) }
    return destination
}

/**
 * Populates and returns the [destination] mutable map with key-value pairs,
 * where key is provided by the [keySelector] function and
 * and value is provided by the [valueTransform] function applied to elements of the given collection.
 *
 * If any two elements would have the same key returned by [keySelector] the last one gets added to the map and the method creates a warning.
 */
inline fun <T, K, V, M : MutableMap<in K, in V>> Sequence<T>.assocByTo(destination: M, keySelector: (T) -> K, valueTransform: (T) -> V): M {
    var size = 0
    for (element in this) {
        destination.put(keySelector(element), valueTransform(element))
        size++
    }
    destination.checkUniqueness(size) { this.groupBy(keySelector, valueTransform) }
    return destination
}

/**
 * Populates and returns the [destination] mutable map with key-value pairs
 * provided by [transform] function applied to each element of the given collection.
 *
 * If any of two pairs would have the same key the last one gets added to the map and the method creates a warning.
 */
inline fun <T, K, V, M : MutableMap<in K, in V>> Sequence<T>.assocTo(destination: M, transform: (T) -> Pair<K, V>): M {
    var size = 0
    for (element in this) {
        destination += transform(element)
        size++
    }
    destination.checkUniqueness(size) { this.groupBy({ transform(it).first }, { transform(it).second }) }
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
inline fun <K, V> Sequence<K>.assocWith(valueSelector: (K) -> V): Map<K, V> {
    val result = LinkedHashMap<K, V>(defaultMapCapacity())
    return assocWithTo(result, valueSelector)
}

/**
 * Populates and returns the [destination] mutable map with key-value pairs for each element of the given collection,
 * where key is the element itself and value is provided by the [valueSelector] function applied to that key.
 *
 * If any two elements are equal, the last one overwrites the former value in the map and the method creates a warning.
 */
inline fun <K, V, M : MutableMap<in K, in V>> Sequence<K>.assocWithTo(destination: M, valueSelector: (K) -> V): M {
    var size = 0
    for (element in this) {
        destination.put(element, valueSelector(element))
        size++
    }
    destination.checkUniqueness(size) { this.groupBy({ it }, valueSelector) }
    return destination
}

/**
 * Computes default capacity for the given sequence.
 */
@PublishedApi
internal fun <T> Sequence<T>.defaultMapCapacity() =
    mapCapacity(collectionSizeOrDefault(DEFAULT_COLLECTION_SIZE)).coerceAtLeast(DEFAULT_COERCE_MINIMUM_VALUE)


/**
 * Returns the size of this iterable if it is known, or the specified [default] value otherwise.
 */
@PublishedApi
internal fun <T> Sequence<T>.collectionSizeOrDefault(default: Int): Int = if (this is Collection<*>) this.size else default
