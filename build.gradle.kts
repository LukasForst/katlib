import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Versions.kotlin
}

group = "ai.blindspot.ktoolz"
version = "0.0.1"

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
