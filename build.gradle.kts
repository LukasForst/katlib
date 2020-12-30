import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.jvm.tasks.Jar
import java.net.URL

plugins {
    kotlin("jvm") version "1.4.21"
    `maven-publish`
    id("net.nemerosa.versioning") version "2.14.0"
    id("org.jetbrains.dokka") version "1.4.20"
    id("io.gitlab.arturbosch.detekt") version "1.15.0"
    id("com.jfrog.bintray") version "1.8.5"

}

group = "pw.forst.tools"
version = (versioning.info?.tag ?: versioning.info?.lastTag ?: versioning.info?.build) +
        if (versioning.info?.dirty == true) "-dirty" else "development"


repositories {
    jcenter()
}

dependencies {
    implementation("io.github.microutils", "kotlin-logging", "2.0.4")

    val jacksonVersion = "2.12.0"
    compileOnly("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)
    compileOnly("com.fasterxml.jackson.module", "jackson-module-kotlin", jacksonVersion)

    // testing
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation(kotlin("stdlib-jdk8"))
    testImplementation("io.mockk", "mockk", "1.10.4") // mock framework
    testImplementation("ch.qos.logback", "logback-classic", "1.3.0-alpha5") // logging framework for the tests

    val junitVerion = "5.7.0"
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
