import org.gradle.jvm.tasks.Jar
import java.net.URL


plugins {
    kotlin("jvm") version "1.5.0"

    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"

    id("net.nemerosa.versioning") version "2.14.0"
    id("org.jetbrains.dokka") version "1.4.32"
    id("io.gitlab.arturbosch.detekt") version "1.17.0"
}

group = "pw.forst"
base.archivesBaseName = "katlib"
version = (versioning.info?.tag ?: versioning.info?.lastTag ?: versioning.info?.build) ?: "SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val jacksonVersion = "2.12.3"
    compileOnly("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)
    compileOnly("com.fasterxml.jackson.module", "jackson-module-kotlin", jacksonVersion)
    compileOnly("org.jetbrains.kotlin", "kotlin-reflect", "1.5.0")

    // testing
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation(kotlin("stdlib-jdk8"))
    testImplementation("io.mockk", "mockk", "1.11.0") // mock framework
    testImplementation("ch.qos.logback", "logback-classic", "1.3.0-alpha5") // logging framework for the tests

    val junitVerion = "5.7.1"
    testImplementation("org.junit.jupiter", "junit-jupiter-api", junitVerion) // junit testing framework
    testImplementation("org.junit.jupiter", "junit-jupiter-params", junitVerion) // generated parameters for tests

    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", junitVerion) // testing runtime
}

detekt {
    parallel = true
    input = files("$rootDir/src")
    config = files(rootDir.resolve("detekt-config.yml"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
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
                url.set("https://katlib.forst.pw")
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
                        email.set("lukas@forst.pw")
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
