import org.gradle.jvm.tasks.Jar
import java.net.URL


plugins {
    kotlin("jvm") version "1.8.10"

    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"

    // 3.0.0 requires JVM 11, but we want to keep this compatible with JVM 8
    id("net.nemerosa.versioning") version "2.15.1"
    id("org.jetbrains.dokka") version "1.7.20"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
}

group = "dev.forst"
base.archivesName.set("katlib")
version = (versioning.info?.tag ?: versioning.info?.lastTag ?: versioning.info?.build) ?: "SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // compile only detekt plugin
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.21.0")

    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    compileOnly("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
    compileOnly(kotlin("reflect"))
    compileOnly(kotlin("stdlib-jdk8"))

    // testing
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation(kotlin("stdlib-jdk8"))
    testImplementation("io.mockk:mockk:1.13.4") // mock framework
    @Suppress("GradlePackageUpdate") // we want to run tests on JVM 8, so we will stay on 1.3.x version
    testImplementation("ch.qos.logback:logback-classic:1.3.3") // logging framework for the tests

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2") // junit testing framework
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2") // generated parameters for tests
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2") // testing runtime
}

detekt {
    parallel = true
    source = files("$rootDir/src")
    config = files(rootDir.resolve("detekt-config.yml"))
}

tasks {
    compileJava {
        targetCompatibility = "1.8"
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestJava {
        targetCompatibility = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    test {
        useJUnitPlatform()
    }

    dokkaHtml {
        outputDirectory.set(File("$buildDir/docs"))

        dokkaSourceSets {
            configureEach {
                displayName.set("Katlib")

                sourceLink {
                    localDirectory.set(file("src/main/kotlin"))
                    remoteUrl.set(URL("https://github.com/LukasForst/katlib/tree/master/src/main/kotlin"))
                    remoteLineSuffix.set("#L")
                }
            }
        }
    }
}

// ------------------------------------ Deployment Configuration  ------------------------------------
// deployment configuration - deploy with sources and documentation
val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val javadocJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc)
}

// name the publication as it is referenced
val publication = "mavenJava"
publishing {
    // create jar with sources and with javadoc
    publications {
        create<MavenPublication>(publication) {
            from(components["java"])
            artifact(sourcesJar)
            artifact(javadocJar)

            pom {
                name.set("Katlib")
                description.set("Kotlin Additional Library - useful extension functions")
                url.set("https://katlib.forst.dev")
                packaging = "jar"
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://mit-license.org/license.txt")
                    }
                }
                developers {
                    developer {
                        id.set("lukasforst")
                        name.set("Lukas Forst")
                        email.set("lukas@forst.dev")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/LukasForst/katlib.git")
                    url.set("https://github.com/LukasForst/katlib")
                }
            }
        }
    }
}

signing {
    val signingKeyId = project.findProperty("gpg.keyId") as String? ?: System.getenv("GPG_KEY_ID")
    val signingKey = project.findProperty("gpg.key") as String? ?: System.getenv("GPG_KEY")
    val signingPassword = project.findProperty("gpg.keyPassword") as String? ?: System.getenv("GPG_KEY_PASSWORD")

    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    sign(publishing.publications[publication])
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(project.findProperty("ossrh.username") as String? ?: System.getenv("OSSRH_USERNAME"))
            password.set(project.findProperty("ossrh.password") as String? ?: System.getenv("OSSRH_PASSWORD"))
        }
    }
}
