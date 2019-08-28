/**
 * The list of files ignored from static analysis.
 */
object IgnoredFiles {
    private val common = listOf<String>()

    /**
     * Files ignored by Sonar.
     */
    val sonar = common

    /**
     * Files ignored by Detekt.
     */
    val detekt = common

    /**
     * Files ignored by Jacoco code coverage tool.
     */
    val jacoco = common
}
