# Katlib

![CI test](https://github.com/LukasForst/katlib/workflows/CI%20test/badge.svg)
[![Release pipeline](https://github.com/LukasForst/katlib/actions/workflows/release.yml/badge.svg)](https://github.com/LukasForst/katlib/actions/workflows/release.yml)
[![Documentation](https://img.shields.io/badge/docs-online-brightgreeb)](https://katlib.forst.dev/)

Successor of [Ktoolz](https://github.com/blindspot-ai/ktoolz).

Collection of Kotlin extension functions and utilities. This library does not have any dependency.

## Using Katlib

Katlib is available on the Maven Central. Then to import Katlib to Gradle project use:

```Kotlin
implementation("dev.forst", "katlib", "2.2.7")
```

Or with Groovy DSL

```groovy
implementation 'dev.forst:katlib:2.2.7'
```

To import Katlib to Maven project use:

```xml

<dependency>
    <groupId>dev.forst</groupId>
    <artifactId>katlib</artifactId>
    <version>2.2.7</version>
</dependency>
```

## Documentation

Available online - [katlib.forst.dev](https://katlib.forst.dev/)

## Contribution

Feel free to submit PR with your favourite extension functions and other cool utilities!

## Examples

The library contains a lot of useful (as well as useless) extensions and functions that were gathered during my (and my colleges) Kotlin
career. Please see [tests](src/test/kotlin/dev/forst/katlib) folder for all possible functions and how to use them. Full documentation can
be found [here](https://katlib.forst.dev/).

Please note that some functions seems like duplicates of the standard library - usually it is not like that as they provide similar
functionality on different interface. For example there's `List.random` but not `Iterable.random` - random on `Iterable` is then implemented
in this library. Some functions are also essentially type aliases (for example `equalsIgnoreCase`) for standard library as I was having hard
time finding correct name.

However, in some cases there might be duplicates, as development of this library started in 2018 and a lot of functions came to standard
library in Kotlin 1.4. - so if you find some duplicates, let me know or create PR that deprecates them in the code.

Following functions are the most popular ones.

#### [Iterable Extensions](src/main/kotlin/dev/forst/katlib/IterableExtensions.kt)

* `Iterable<E>.random` - returns the random element from the iterable
* `Iterable<T>.reduction` - reduce producing list allowing you to set initial value, useful for cumulative sums
* `Iterable<List<Int>>.sumByIndexes` - sums all Lists of integers into single one by indexes
* `Iterable<List<Double>>.sumDoublesByIndexes` - same as previous but with doubles
* `Iterable<T>.mapToSet` - creates set from iterable, transforms with given function
* `Iterable<T>.dominantValueBy` - returns the most frequently occurring value of the given function
* `Iterable<T1>.cartesianProduct` - cartesian product between all the elements from two iterables
* `Iterable<T?>.forEachNotNull` - performs action on each element that is not null
* `Iterable<Iterable<T>?>.union` - creates union of all iterables
* `Iterable<Iterable<T>?>.intersect` - creates intersect of all iterables
* `Iterable<T?>.filterNotNullBy` - returns list with only not null results using selector
* `Iterable<T>.singleOrEmpty` - returns single element or null if it wasn't found, throws exception if there are multiple elements
* `Iterable<Pair<T, V>>.splitPairCollection` - returns pair of two lists with left and right values
* `Iterable<T>.setDifferenceBy` - returns difference between two iterables
* `Iterable<Pair<K, V>>.assoc` - returns map from key value pairs but logs case when key is replaced by same key with different value
* `Iterable<Triple<A, B, C>>.flattenToLists` - returns three lists constructed from triples
* `Iterable<T>.toNavigableSet` - creates NavigableSet
* `Iterable<T>.isEmpty` - determines whether the iterable is empty or not
* `Iterable<Iterable<T>>.cartesianProduct` - cartesian product between all the elements from nested iterables
* `Iterable<Iterable<T>>.lazyCartesianProduct` - cartesian product between all the elements from nested iterables as sequence
* `Iterable<A>.zip(b: Iterable<B>, c: Iterable<C>, transform: (a: A, b: B, c: C) -> V)` - zip with three collections instead of two
* `Iterable<T>.sumByFloat(selector: (T) -> Float)` - sums iterable by float selector, because `sumOf` from stdlib does not have
  implementation for Floats
* `Iterable<T>.withEach(action: T.() -> Unit)` - performs the given action with each element as a receiver
* `Iterable<T>.withEachIndexed(action: T.(index: Int) -> Unit)` - performs the given action with each element as a receiver, providing sequential index with the element

#### [Map Extensions](src/main/kotlin/dev/forst/katlib/MapExtensions.kt)

* `Map<T, Double>.getWeightedRandom` - randomly selects item with respect to the current weight distribution
* `Map<K, V>.mergeReduce` - two maps together using the given reduce function
* `Map<K, V1>.join` - joins two maps together using the given join function
* `Map<K1, Map<K2, V>>.swapKeys(): Map<K2, Map<K1, V>>` - swaps keys in two-dimensional maps
* there are multiple `swapKeys` implementations for up to three-dimensional maps, just
  browse [the code](src/main/kotlin/dev/forst/katlib/MapExtensions.kt)
* `Map<Pair<K1, K2>, V>.toTwoLevelMap(): Map<K1, Map<K2, V>>` - creates two-dimensional map from the map of pairs
* `Map<Triple<K1, K2, K3>, V>.toThreeLevelMap(): Map<K1, Map<K2, Map<K3, V>>>` - creates three-dimensional map from the map of triples
* `Map<K1, Map<K2, V>>.getSecondLevelValues(): Set<V>` - collects all the values from the bottom level into set
* `Map<K1, Map<K2, Map<K3, V>>>.getThirdLevelValues(): Set<V>` - collects all the values from the bottom level into set
* `Iterable<Map<K, V>>.merge(): Map<K, List<V>>` - for each key, merges all the values into one common list

#### [Set Extensions](src/main/kotlin/dev/forst/katlib/SetExtensions.kt)

* `SortedSet.min` - returns minimum of the set or null if empty
* `SortedSet.max` - returns maximum of the set or null

#### [Pair Extensions](src/main/kotlin/dev/forst/katlib/PairExtensions.kt)

* `mapLeft/Right/Pair` - applies given block to left/right/all iterable element/s of the pair

```kotlin
val pair = Pair(listOf(1, 2, 3), 0)
assertEquals(Pair(listOf(11, 12, 13), 0), pair.mapLeft { it + 10 })
```

* `letLeft/Right/Pair` - applies given block to left/right/all element/s of the pair

```kotlin
val pair = Pair(10, 20)
assertEquals(Pair("10", 20), pair.letLeft { it.toString() })
```

#### [Date Extensions](src/main/kotlin/dev/forst/katlib/DateExtensions.kt)

* `getDateRangeTo` - returns list of dates between two `LocalDate`
* `getDaysInInterval` - returns number of days between two `LocalDate` (inclusive)
* `getDayDifference` - returns number of days between two `LocalDate` (exclusive)
* `getWeekOfYear` - returns week of year for given `LocalDate` and optionaly `Locale`

#### [Jackson Extensions](src/main/kotlin/dev/forst/katlib/JacksonExtensions.kt)

To use these, one must include dependency on Jackson

```kotlin
implementation("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)
implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", jacksonVersion)
```

* `jacksonMapper` - creates jackson mapper with some reasonable settings
* `parseJson` - parses JSON from the string/bytes, returns either instance of null, can log exception if some occurs

```kotlin
val obj: MyDataClass? = parseJson<MyDataClass>(myJson)
```

* `createJson` - creates JSON from given object
* `createPrettyJson` - creates JSON with pretty print
* `createJsonBytes` - creates JSON in bytes from given object
* `prettyPrintJson` - returns pretty printed JSON value as string

#### [Boolean Extensions](src/main/kotlin/dev/forst/katlib/BooleanExtensions.kt)

`whenTrue` and `whenFalse` - useful extensions mainly used for logging when the oneliners are used.

```kotlin
fun someFunctionIndicatingSuccess(): Boolean =
    someComputationReturningBoolean()
        .whenFalse {
            logger.warning { "someComputationReturningBoolean returned false! Computation probably failed" }
        }
```

#### [String Extensions](src/main/kotlin/dev/forst/katlib/StringExtensions.kt)

* `startsWithLetter` - returns true fi string starts with latin letter a-z or A-Z
* `restrictLengthWithEllipsis` - shortens the string to given max length, appends ellipsis

```kotlin
assertEquals("ABCD…", "ABCDEFHG".restrictLengthWithEllipsis(5, "..."))
``` 

* `toUuid` - converts string to UUID

#### [Instant Extensions](src/main/kotlin/dev/forst/katlib/InstantExtensions.kt)

* `durationToInMilli` - returns absolute difference between two `Instant` values in milliseconds

#### [Crypto Extensions](src/main/kotlin/dev/forst/katlib/CryptoExtensions.kt)

* `hashWithSha256` - produces `SHA-256` of given string/file/bytes
* `computeMd5` - computes MD5 from given byte array, returns base64 encoded data

#### [Miscellaneous Extensions](src/main/kotlin/dev/forst/katlib/OtherExtensions.kt)

* `Optional<T>.orNull(): T?` - from optional to Kotlin optional
* `T.whenNull` - executes block when `this` is null, useful for logging

```kotlin
fun someFunction(): String? =
    produceOptionalString()
        .whenNull { logger.warn { "produceOptionalString returned null value!" } }
```

* `T.asList` - from `this creates one element list
* `ClosedRange<T>.intersects` - intersection between ranges
* `T.with` - bundles two objects to list
* `validate` - executes invalid block if validating block returns false, useful for validation

```kotlin
validate(
    { someText.startsWith("something") && someText.endsWith("else") },
    { throw IllegalStateException() }
)
```

* `Pair<A?, B?>.propagateNull(): Pair<A, B>?` - if left or right is null, returns null, otherwise pair
* `T.applyIf` - applies given block only if should apply block returns true

```kotlin
byteBuffer.applyIf(shouldReadInt) { getInt() }
```

* `isUuid` - returns true if given string is UUID
* `isUrl` - returns true if give string is URL (with some limitations, see docs)
* `getEnv` - shortcut for `System.getenv`
* `newLine` - shortcut for `System.lineSeparator`
* `ByteArray.toUuid` - Read ByteArray as two longs and combine the to UUID

#### [Services](src/main/kotlin/dev/forst/katlib/Services.kt)

* `TemporalProvider` - Interface providing access to current time via `now` method, very useful when mocking

#### [Array Extensions](src/main/kotlin/dev/forst/katlib/ArrayExtensions.kt)

* `buildArray(builderAction: MutableList<E>.() -> Unit): Array<E>` - builds a new Array by populating a MutableList using the given builderAction and returning an Array with the same elements
* `Array<out T>.map(transform: (T) -> R): Array<R>` - Returns an array containing the results of applying the given transform function to each element in the original array
* `Array<out T>.mapIndexed(transform: (index: Int, T) -> R): Array<R>` - returns an array containing the results of applying the given transform function to each element and its index in the original array
* `Array<out T>.filter(predicate: (T) -> Boolean): Array<T>` - returns an array containing only elements matching the given predicate
* `Array<out T>.filterNot(predicate: (T) -> Boolean): Array<T>` - returns an array containing all elements not matching the given predicate
* `Array<out T>.filterIndexed(predicate: (index: Int, T) -> Boolean): Array<T>` - returns an array containing only elements matching the given predicate
* `Array<*>.filterIsInstance(): Array<R>` - returns an array containing all elements that are instances of specified type parameter R
* `Array<out T?>.filterNotNull(): Array<T>` - returns an array containing all elements that are not `null`
* `Array<out T>.minus(element: T): Array<T>` - returns an array containing all elements of the original collection without the first occurrence of the given element
* `Array<out T>.minus(elements: Array<out T>): Array<T>` - returns an array containing all elements of the original collection except the elements contained in the given elements array

#### [Prompt](src/main/kotlin/dev/forst/katlib/Prompt.kt)

* `prompt(promptText: String, exceptionHandler: (e: Exception) -> String? = { null }, transform: (input: String) -> R): R` - prompts user and applies transform to input, invokes exceptionHandler if transform threw an Exception, and repeats prompt
