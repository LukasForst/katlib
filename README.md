# ktoolz
Collection of Kotlin extension functions and utilities

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
