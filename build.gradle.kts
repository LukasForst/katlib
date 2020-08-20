import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.jvm.tasks.Jar

plugins {
    kotlin("jvm") version "1.4.0"
    `maven-publish`
    id("net.nemerosa.versioning") version "2.14.0"
    id("org.jetbrains.dokka") version "1.4.0-rc"
    id("io.gitlab.arturbosch.detekt") version "1.11.2"
    id("com.jfrog.bintray") version "1.8.5"

}

group = "pw.forst.tools"
version = "0.0.1"


repositories {
    jcenter()
}

dependencies {
    implementation("io.github.microutils", "kotlin-logging", "1.8.3") {
        // until kotlin-logging has support for kotlin 1.4
        exclude("org.jetbrains.kotlin", "kotlin-stdlib")
        exclude("org.jetbrains.kotlin", "kotlin-stdlib-common")
    }

    // all dependencies must me compileOnly as this is library
    compileOnly(kotlin("stdlib-jdk8")) // kotlin std

    val jacksonVersion = "2.11.2"
    compileOnly("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)
    compileOnly("com.fasterxml.jackson.module", "jackson-module-kotlin", jacksonVersion)
    // because jackson kotlin have a bit older lib
    compileOnly("org.jetbrains.kotlin", "kotlin-reflect", "1.4.0")
    // testing
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation(kotlin("stdlib-jdk8"))
    testImplementation("io.mockk", "mockk", "1.10.0") // mock framework
    testImplementation("ch.qos.logback", "logback-classic", "1.3.0-alpha5") // logging framework for the tests

    val junitVerion = "5.6.2"
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
        outputDirectory = "$buildDir/docs"

        dokkaSourceSets {
            configureEach {
                moduleDisplayName = "katlib"
                displayName = "Katlib"

                sourceLink {
                    path = "src/main/kotlin"
                    url = "https://github.com/LukasForst/katlib/blob/master/src/main/kotlin"
                }
            }
        }
    }
}

// ------------------------------------ Deployment Configuration  ------------------------------------
val githubRepository = "LukasForst/katlib"
val descriptionForPackage = "Kotlin Additional Library - usefull extension functions"
val tags = arrayOf("kotlin", "extension functions")
// everything bellow is set automatically

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
val publication = "default-gradle-publication"
publishing {
    // create jar with sources and with javadoc
    publications {
        register(publication, MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar)
            artifact(javadocJar)
        }
    }

    // publish package to the github packages
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/$githubRepository")
            credentials {
                username = project.findProperty("gpr.user") as String?
                    ?: System.getenv("GITHUB_USERNAME")
                password = project.findProperty("gpr.key") as String?
                    ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

// upload to bintray
bintray {
    // env variables loaded from pipeline for publish
    user = project.findProperty("bintray.user") as String?
        ?: System.getenv("BINTRAY_USER")
    key = project.findProperty("bintray.key") as String?
        ?: System.getenv("BINTRAY_TOKEN")
    publish = true
    setPublications(publication)
    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        // my repository for maven packages
        repo = "jvm-packages"
        name = project.name
        // my user account at bintray
        userOrg = "lukas-forst"
        websiteUrl = "https://katlib.forst.pw"
        githubRepo = githubRepository
        vcsUrl = "https://github.com/$githubRepository"
        description = descriptionForPackage
        setLabels(*tags)
        setLicenses("MIT")
        desc = description
    })
}
