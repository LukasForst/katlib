@file:Suppress("RemoveExplicitTypeArguments")

// the compiler sometimes needs more information about the types

package pw.forst.tools.katlib

import java.util.Random


/**
 * Randomly selects item with respect to the current weight distribution.
 */
fun <T> Map<T, Double>.getWeightedRandom(rand: Random): T {
    val total = this.values.sum()
    return if (total == 0.0) {
        this.keys.getRandomElement(rand)
    } else {
        val r = rand.nextDouble() * total
        var countWeight = 0.0

        for ((item, weight) in this) {
            countWeight += weight
            if (countWeight > r) {
                return item
            }
        }
        this.keys.last()
    }
}


/**
 * Randomly sorts the items with respect to the current weight distribution. [normalizer] is used to normalize the random value obtained from [rand] to control
 * how important the original weights are. With higher [normalizer] the importance of the random factor decreases.
 */
fun <T> Map<T, Double>.getKeysInWeightedRandomOrder(normalizer: Double, rand: Random): List<T> {
    return this.mapValues { (rand.nextDouble() + normalizer) * it.value }.entries.sortedByDescending { it.value }.map { it.key }
}

/**
 * Merges two maps together using the given [reduce] function. By default, the reduce function keeps the value from [this] map.
 */
inline fun <K, V> Map<K, V>.mergeReduce(other: Map<K, V>, reduce: (V, V) -> V = { a, _ -> a }): Map<K, V> {
    return mergeReduceTo(LinkedHashMap(this.size + other.size), other, reduce)
}

/**
 * Merges two maps together using the given [reduce] function into the given [destination]. By default, the reduce function keeps the value from [this] map.
 */
inline fun <K, V, M : MutableMap<K, V>> Map<K, V>.mergeReduceTo(destination: M, other: Map<K, V>, reduce: (V, V) -> V = { a, _ -> a }): M {
    destination.putAll(this)
    for ((key, value) in other) {
        destination[key] = destination[key]?.let { reduce(it, value) } ?: value
    }
    return destination
}

/**
 * Joins two maps together using the given [join] function.
 */
inline fun <K, V1, V2, VR> Map<K, V1>.join(other: Map<K, V2>, join: (V1?, V2?) -> VR): Map<K, VR> {
    return joinTo(LinkedHashMap(this.size + other.size), other, join)
}

/**
 * Joins two maps together using the given [join] function into the given [destination].
 */
inline fun <K, V1, V2, VR, M : MutableMap<K, VR>> Map<K, V1>.joinTo(destination: M, other: Map<K, V2>, join: (V1?, V2?) -> VR): M {
    val keys = this.keys + other.keys
    for (key in keys) {
        destination[key] = join(this[key], other[key])
    }
    return destination
}

/**
 * Swaps dimensions in two dimensional map. The returned map has keys from the second dimension as primary keys and primary keys are used in the second
 * dimension.
 */
fun <K1, K2, V> Map<K1, Map<K2, V>>.swapKeys(): Map<K2, Map<K1, V>> = swapKeysTo(LinkedHashMap()) { LinkedHashMap<K1, V>() }

/**
 * Swaps dimensions in two dimensional map. The returned map has keys from the second dimension as primary keys and primary keys are stored in the second
 * dimension. [topDestination] specifies which map should be used to store the new primary keys and [bottomDestination] is used to store the new secondary keys.
 */
inline fun <K1, K2, V, M2 : MutableMap<K1, V>, M1 : MutableMap<K2, M2>> Map<K1, Map<K2, V>>.swapKeysTo(
    topDestination: M1,
    bottomDestination: () -> M2
): M1 {
    for ((key1, map) in this) {
        for ((key2, value) in map) {
            topDestination.getOrPut(key2, bottomDestination)[key1] = value
        }
    }
    return topDestination
}

/**
 * Works similarly as [swapKeys] but the final map has three levels. [transform] specifies how the keys should be swapped (or modified) in the newly created
 * map.
 */
inline fun <K1, K2, K3, KR1, KR2, KR3, V> Map<K1, Map<K2, Map<K3, V>>>.swapKeys(transform: (K1, K2, K3) -> Triple<KR1, KR2, KR3>):
        Map<KR1, Map<KR2, Map<KR3, V>>> =
    this.swapKeysTo(LinkedHashMap(), { LinkedHashMap<KR2, MutableMap<KR3, V>>() }, { LinkedHashMap() }, transform)

/**
 * Works similarly as [swapKeys] but the final map has three levels. [transform] specifies how the keys should be swapped (or modified) in the newly created
 * map. [topDestination] specifies which map should be used to store the new primary keys, [middleDestination] is used to store the new secondary keys and
 * [bottomDestination] is used to store the new tertiary keys.
 */
inline fun <K1, K2, K3, KR1, KR2, KR3, V, M3 : MutableMap<KR3, V>, M2 : MutableMap<KR2, M3>, M1 : MutableMap<KR1, M2>> Map<K1, Map<K2, Map<K3, V>>>.swapKeysTo(
    topDestination: M1,
    middleDestination: () -> M2,
    bottomDestination: () -> M3,
    transform: (K1, K2, K3) -> Triple<KR1, KR2, KR3>
): M1 {
    for ((key1, map2) in this) {
        for ((key2, map3) in map2) {
            for ((key3, value) in map3) {
                val (kr1, kr2, kr3) = transform(key1, key2, key3)
                topDestination.getOrPut(kr1, middleDestination).getOrPut(kr2, bottomDestination)[kr3] = value
            }
        }
    }
    return topDestination
}

/**
 * Transforms map of pairs as a keys into two dimensional map where the first elements in the pair are used as primary keys and second elements as secondary
 * keys.
 */
fun <K1, K2, V> Map<Pair<K1, K2>, V>.toTwoLevelMap(): Map<K1, Map<K2, V>> = toTwoLevelMap(LinkedHashMap()) { LinkedHashMap<K2, V>() }

/**
 * Transforms map of pairs as a keys into two dimensional map where the first elements in the pair are used as primary keys and second elements as secondary
 * keys. [topDestination] specifies which map should be used to store the new primary keys and [bottomDestination] is used to store the new secondary keys.
 */
inline fun <K1, K2, V, M2 : MutableMap<K2, V>, M1 : MutableMap<K1, M2>> Map<Pair<K1, K2>, V>.toTwoLevelMap(
    topDestination: M1,
    bottomDestination: () -> M2
): M1 {
    for ((key, value) in this) {
        val (key1, key2) = key
        topDestination.getOrPut(key1, bottomDestination)[key2] = value
    }
    return topDestination
}

/**
 * Transforms list of pairs, where the first element consists of pairs into two dimensional map where the first elements from the inner pair are used as
 * primary keys and second elements as secondary keys.
 */
fun <K1, K2, V> List<Pair<Pair<K1, K2>, V>>.toTwoLevelMap(): Map<K1, Map<K2, V>> = toTwoLevelMap(LinkedHashMap()) { LinkedHashMap<K2, V>() }

/**
 * Transforms list of pairs, where the first element consists of pairs into two dimensional map where the first elements from the inner pair are used as
 * primary keys and second elements as secondary keys. [topDestination] specifies which map should be used to store the new primary keys and
 * [bottomDestination] is used to store the new secondary keys.
 */
inline fun <K1, K2, V, M2 : MutableMap<K2, V>, M1 : MutableMap<K1, M2>> List<Pair<Pair<K1, K2>, V>>.toTwoLevelMap(
    topDestination: M1,
    bottomDestination: () -> M2
): M1 {
    for ((key, value) in this) {
        val (key1, key2) = key
        topDestination.getOrPut(key1, bottomDestination)[key2] = value
    }
    return topDestination
}

/**
 * Works similarly as [toTwoLevelMap] but the final map has three levels.
 */
fun <K1, K2, K3, V> Map<Triple<K1, K2, K3>, V>.toThreeLevelMap(): Map<K1, Map<K2, Map<K3, V>>> =
    toThreeLevelMap(LinkedHashMap(), { LinkedHashMap<K2, MutableMap<K3, V>>() }, { LinkedHashMap() })

/**
 * Works similarly as [toTwoLevelMap] but the final map has three levels. [topDestination] specifies which map should be used to store the new primary keys,
 * [middleDestination] is used to store the new secondary keys and [bottomDestination] is used to store the new tertiary keys.
 */
inline fun <K1, K2, K3, V, M3 : MutableMap<K3, V>, M2 : MutableMap<K2, M3>, M1 : MutableMap<K1, M2>> Map<Triple<K1, K2, K3>, V>.toThreeLevelMap(
    topDestination: M1,
    middleDestination: () -> M2,
    bottomDestination: () -> M3
): M1 {
    for ((key, value) in this) {
        val (key1, key2, key3) = key
        topDestination.getOrPut(key1, middleDestination).getOrPut(key2, bottomDestination)[key3] = value
    }
    return topDestination
}

/**
 * Collects all the values from the bottom level into set.
 */
fun <K1, K2, V> Map<K1, Map<K2, V>>.getSecondLevelValues(): Set<V> = this.getSecondLevelValuesTo(LinkedHashSet())

/**
 * Collects all the values from the bottom level into the given collection [M].
 */
fun <K1, K2, V, M : MutableCollection<V>> Map<K1, Map<K2, V>>.getSecondLevelValuesTo(destination: M): M {
    for ((_, bottomMap) in this) {
        destination.addAll(bottomMap.values)
    }
    return destination
}

/**
 * Collects all the values from the bottom level into set.
 */
fun <K1, K2, K3, V> Map<K1, Map<K2, Map<K3, V>>>.getThirdLevelValues(): Set<V> = this.getThirdLevelValuesTo(LinkedHashSet())

/**
 * Collects all the values from the bottom level into the given collection [M].
 */
fun <K1, K2, K3, V, M : MutableCollection<V>> Map<K1, Map<K2, Map<K3, V>>>.getThirdLevelValuesTo(destination: M): M {
    for ((_, secondLevelMap) in this) {
        for ((_, bottomMap) in secondLevelMap) {
            destination.addAll(bottomMap.values)
        }
    }
    return destination
}

/**
 * For each key, merges all the values into one common list.
 */
fun <K, V : Any> Iterable<Map<K, V>>.merge(): Map<K, List<V>> {
    val keys = this.map { it.keys }.union()
    return keys.assocWith { key -> this.mapNotNull { map -> map[key] } }
}

/**
 * For each key, merges all the list into one common list.
 */
fun <K, V : Any> Iterable<Map<K, List<V>>>.flatMerge(): Map<K, List<V>> {
    val keys = this.map { it.keys }.union()
    return keys.assocWith { key -> this.flatMap { map -> map[key] ?: emptyList() } }
}


