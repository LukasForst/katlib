# ktoolz
Collection of Kotlin extension functions and utilities

## Using Ktoolz
To import Ktoolz to Gradle project use:
```Kotlin
implementation("ai.blindspot.ktoolz:ktoolz:0.0.4")
```
Or with Groovy DSL
```groovy
implementation 'ai.blindspot.ktoolz:ktoolz:0.0.4'
```
To import Ktoolz to Maven project use:
```xml
<dependency>
  <groupId>ai.blindspot.ktoolz</groupId>
  <artifactId>ktoolz</artifactId>
  <version>0.0.4</version>
</dependency>
```



## Deployment
To deploy the library to Nexus repository one must set up deployment secrets,
the secrets should be stored in the file `ktoolz.properties`, that is not tracked by the git.
The file has following structure:
```properties
nexus.url.snapshots=<fill.me>
nexus.url.releases=<fill.me>
nexus.username=<fill.me>
nexus.password=<fill.me>
```
To publish the library, one must execute `./gradlew publish` or using the `Makefile` - `make publish`.

To release new version of the Ktoolz simply call `make release VERSION=0.0.0` where `0.0.0` is the new version. 
