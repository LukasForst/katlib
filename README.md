# Katlib

Successor of [Ktoolz](https://github.com/blindspot-ai/ktoolz).

Collection of Kotlin extension functions and utilities. 

## Using Katlib
Katlib is hosted on [JCenter](https://bintray.com) and therefore one must include to the project.
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
