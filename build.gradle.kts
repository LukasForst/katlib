import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    jacoco // for test coverage reports

    kotlin("jvm") version Versions.kotlin

    id("io.gitlab.arturbosch.detekt") version Versions.detekt
    id("com.jfrog.bintray") version Versions.binTrayPlugin
}

group = "ai.blindspot.ktoolz"
version = "1.0.6"

repositories {
    jcenter()
}

detekt {
    parallel = true
    input = files(subprojects.map { it.projectDir }, "buildSrc")
    config = files(rootDir.resolve("detekt-config.yml"))
}

dependencies {
    implementation(Libs.kotlinLogging) // logging DSL - must be implementation

    // all dependencies must me compileOnly as this is library
    compileOnly(kotlin("stdlib-jdk8")) // kotlin std
    compileOnly("com.fasterxml.jackson.core", "jackson-databind", "2.10.3")
    compileOnly("com.fasterxml.jackson.module", "jackson-module-kotlin", "2.10.3")

    // testing
    testImplementation(TestLibs.kotlinTest) // kotlin idiomatic testing
    testImplementation(TestLibs.kotlinTestJunit5) // kotlin.test wrapper for Junit5
    testImplementation(TestLibs.mockk) // mock framework
    testImplementation(TestLibs.logbackClassic) // logging framework for the tests
    testImplementation(TestLibs.junitApi) // junit testing framework
    testImplementation(TestLibs.junitParams) // generated parameters for tests

    testRuntimeOnly(TestLibs.junitEngine) // testing runtime
}

/**
 * Folder with stored jacoco test coverage results
 */
val jacocoReports = "$buildDir${File.separator}jacoco${File.separator}reports"

tasks {
    // when check is executed, detekt and test coverage verification must be run as well
    check {
        dependsOn(detekt, jacocoTestCoverageVerification) // fails when the code coverage is below value specified in Props.codeCoverageMinimum
    }

    // generate test reports for the Sonarqube and in the human-readable form
    jacocoTestReport {
        dependsOn(test)

        @Suppress("UnstableApiUsage") // Required for test coverage reports, however the api is still incubating
        reports {
            csv.isEnabled = false
            xml.isEnabled = true
            xml.destination = file("$jacocoReports.xml")
            html.isEnabled = true
            html.destination = file(jacocoReports)
        }
    }

    // set up verification for test coverage
    jacocoTestCoverageVerification {
        dependsOn(jacocoTestReport)
    }

    withType<Test> {
        useJUnitPlatform()
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}

// deployment configuration - deploy with sources and documentation
val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val javadocJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc)
}

val publicationName = "ktoolz"
publishing {
    publications {
        register(publicationName, MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar)
            artifact(javadocJar)
        }
    }
}

bintray {
    user = Props.bintrayUser.getOrDefault()
    key = Props.bintrayApiKey.getOrDefault()
    publish = true
    setPublications(publicationName)
    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = "maven"
        name = "ktoolz"
        userOrg = "blindspot-ai"
        websiteUrl = "https://blindspot.ai"
        githubRepo = "blindspot-ai/ktoolz"
        vcsUrl = "https://github.com/blindspot-ai/ktoolz"
        description = "Collection of Kotlin extension functions and utilities."
        setLabels("kotlin")
        setLicenses("MIT")
        desc = description
    })
}
