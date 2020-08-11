# Katlib
[![GitHub version](https://badge.fury.io/gh/LukasForst%2Fkatlib.svg)](https://bintray.com/beta/#/lukas-forst/jvm-packages/katlib)
![CI test](https://github.com/LukasForst/katlib/workflows/CI%20test/badge.svg)

Successor of [Ktoolz](https://github.com/blindspot-ai/ktoolz).

Collection of Kotlin extension functions and utilities. 
This library contains minimal dependencies - just `kotlin-logging` for logging. 

## Using Katlib
Katlib is hosted on [JCenter](https://bintray.com/beta/#/lukas-forst/jvm-packages/katlib) and therefore one must include to the project.
```kotlin
repositories {
    jcenter()
}
```
Then to import Katlib to Gradle project use:
```Kotlin
implementation("pw.forst.tools", "katlib", "some-latest-version")
```
Or with Groovy DSL
```groovy
implementation 'pw.forst.tools:katlib:some-latest-version'
```
To import Katlib to Maven project use:
```xml
<dependency>
  <groupId>pw.forst.tools</groupId>
  <artifactId>katlib</artifactId>
  <version>some-latest-version</version>
</dependency>
```

## Examples
The library contains a lot of useful (as well as useless) extensions and functions 
that were gathered during my (and my colleges) Kotlin career. 
Please see [tests](src/test/kotlin/pw/forst/tools/katlib) folder for all possible functions 
and how to use them.
Following functions are the most popular ones.


#### [Iterable Extensions](src/main/kotlin/pw/forst/tools/katlib/IterableExtensions.kt)
* `Iterable<E>.getRandomElement` - returns the random element from the iterable
* `Iterable<T>.reduction` - reduce producing list, useful for cumulative sums
* `Iterable<T>.sumByLong` - sums iterable by long value with selector
* `Iterable<List<Int>>.sumByIndexes` - sums all Lists of integers into single one by indexes
* `Iterable<List<Double>>.sumDoublesByIndexes` - same as previous but with doubles
* `Iterable<T>.maxValueBy` - returns the largest value of given iterable by provided selector
* `Iterable<T>.minValueBy` - same as previous, but smallest value
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
 
#### [Map Extensions](src/main/kotlin/pw/forst/tools/katlib/MapExtensions.kt)
* `Map<T, Double>.getWeightedRandom` - randomly selects item with respect to the current weight distribution
* `Map<K, V>.mergeReduce` - two maps together using the given reduce function 
* `Map<K, V1>.join` - joins two maps together using the given join function
* `Map<K1, Map<K2, V>>.swapKeys(): Map<K2, Map<K1, V>>` - swaps keys in two-dimensional maps
* there are multiple `swapKeys` implementations for up to three-dimensional maps, just browse [the code](src/main/kotlin/pw/forst/tools/katlib/MapExtensions.kt)
* `Map<Pair<K1, K2>, V>.toTwoLevelMap(): Map<K1, Map<K2, V>>` - creates two-dimensional map from the map of pairs
* `Map<Triple<K1, K2, K3>, V>.toThreeLevelMap(): Map<K1, Map<K2, Map<K3, V>>>` - creates three-dimensional map from the map of triples
* `Map<K1, Map<K2, V>>.getSecondLevelValues(): Set<V>` - collects all the values from the bottom level into set
* `Map<K1, Map<K2, Map<K3, V>>>.getThirdLevelValues(): Set<V>` - collects all the values from the bottom level into set
* `Iterable<Map<K, V>>.merge(): Map<K, List<V>>` - for each key, merges all the values into one common list

#### [Set Extensions](src/main/kotlin/pw/forst/tools/katlib/SetExtensions.kt)
* `SortedSet.min` - returns minimum of the set or null if empty
* `SortedSet.max` - returns maximum of the set or null
 
#### [Pair Extensions](src/main/kotlin/pw/forst/tools/katlib/PairExtensions.kt)
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

#### [Date Extensions](src/main/kotlin/pw/forst/tools/katlib/DateExtensions.kt)
* `getDateRangeTo` - returns list of dates between two `LocalDate`
* `getDaysInInterval` - returns number of days between two `LocalDate` (inclusive)
* `getDayDifference` - returns number of days between two `LocalDate` (exclusive)
* `getWeekOfYear` - returns week of year for given `LocalDate` and optionaly `Locale`
 
#### [Jackson Extensions](src/main/kotlin/pw/forst/tools/katlib/JacksonExtensions.kt)
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

#### [Boolean Extensions](src/main/kotlin/pw/forst/tools/katlib/BooleanExtensions.kt)
`whenTrue` and `whenFalse` - useful extensions mainly used for logging when the onliners are used.  
```kotlin
fun someFunctionIndicatingSuccess(): Boolean = 
    someComputationReturningBoolean()
    .whenFalse { 
        logger.warning { "someComputationReturningBoolean returned false! Computation probably failed" } 
    }
```

#### [String Extensions](src/main/kotlin/pw/forst/tools/katlib/StringExtensions.kt)
* `startsWithLetter` - returns true fi string starts with latin letter a-z or A-Z
* `restrictLengthWithEllipsis` - shortens the string to given max length, appends ellipsis
```kotlin
assertEquals("ABCD…", "ABCDEFHG".restrictLengthWithEllipsis(5, "..."))
``` 

#### [Instant Extensions](src/main/kotlin/pw/forst/tools/katlib/InstantExtensions.kt)
* `durationToInMilli` - returns absolute difference between two `Instant` values in milliseconds

#### [Crypto Extensions](src/main/kotlin/pw/forst/tools/katlib/CryptoExtensions.kt)
* `hashWith256` - produces `SHA-256` of given string/file/bytes.

#### [Miscellaneous Extensions](src/main/kotlin/pw/forst/tools/katlib/OtherExtensions.kt)
* `Optional<T>.orNull(): T?` - from optional to Kotlin optional
* `T.whenNull` - executes block when `this` is null, usefull for logging
```kotlin
fun someFunction(): String? =
    produceOptionalString()
        .whenNull { logger.warn { "produceOptionalString returned null value!" } }
```
* `T.asList` - from `this creates one element list
* `ClosedRange<T>.intersects` - intersection between ranges
* `T.with` - bundles two objects to list
* `validate` - executes invalid block if validating block returns false, usefull for validation
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
* `isUUID` - returns true if given string is UUID
* `iSURL` - returns true if give string is URL (with some limitations, see docs)
* `getEnv` - shortcut for `System.getenv`
* `newLine` - shortcut for `System.lineSeparator`
