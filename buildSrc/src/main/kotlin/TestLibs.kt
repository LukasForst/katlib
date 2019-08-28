/**
 * The libraries used by test source code.
 */
object TestLibs {
    const val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}" // kotlin idiomatic testing
    const val kotlinTestJunit5 = "org.jetbrains.kotlin:kotlin-test-junit5:${Versions.kotlin}" // kotlin.test wrapper for Junit5
    const val mockk = "io.mockk:mockk:${Versions.mockk}" // mock framework

    const val logbackClassic = "ch.qos.logback:logback-classic:${Versions.logBack}" // logging framework

    const val junitApi = "org.junit.jupiter:junit-jupiter-api:${Versions.junit}" // junit testing framework
    const val junitParams = "org.junit.jupiter:junit-jupiter-params:${Versions.junit}" // generated parameters for test
    const val junitEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit}" // engine for running tests
}
