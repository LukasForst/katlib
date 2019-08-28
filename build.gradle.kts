import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    `maven-publish`

    kotlin("jvm") version Versions.kotlin
}

group = "ai.blindspot"
version = "0.0.1-SNAPSHOT"

repositories {
    jcenter()
}

dependencies {
    implementation(Libs.kotlinStdlib) // kotlin std
    implementation(Libs.kotlinLogging) // logging DSL

    // testing
    testImplementation(TestLibs.kotlinTest) // kotlin idiomatic testing
    testImplementation(TestLibs.kotlinTestJunit5) // kotlin.test wrapper for Junit5
    testImplementation(TestLibs.mockk) // mock framework
    testImplementation(TestLibs.logbackClassic) // loging framework for the tests
    testImplementation(TestLibs.junitApi) // junit testing framework
    testImplementation(TestLibs.junitParams) // generated parameters for tests

    testRuntime(TestLibs.junitEngine) // testing runtime
}

tasks.test {
    @Suppress("UnstableApiUsage") // Required for running tests, however the api is still incubating
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val sourcesJar by tasks.creating(Jar::class) {
    classifier = "sources"
    from(sourceSets.main.get().allSource)
}

val javadocJar by tasks.creating(Jar::class) {
    classifier = "javadoc"
    from(tasks.javadoc)
}

publishing {
    publications {
        create<MavenPublication>("default") {
            from(components["java"])
            artifact(sourcesJar)
            artifact(javadocJar)
        }
    }
    repositories {
        maven {
            val releasesRepoUrl = URI(Props.nexusUrlReleases.get())
            val snapshotsRepoUrl = URI(Props.nexusUrlSnapshots.get())

            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl

            credentials {
                username = Props.nexusUser.get()
                password = Props.nexusPassword.get()
            }
        }
    }
}
