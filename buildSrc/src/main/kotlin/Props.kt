/**
 * The properties needed by build and read from `ktoolz.properties` file or environment variables.
 */
object Props {
    val nexusUser = PropertyEntry.create(propertyName = "nexus.username", environmentName = "NEXUS_USERNAME")
    val nexusPassword = PropertyEntry.create(propertyName = "nexus.password", environmentName = "NEXUS_PASSWORD")

    val sonarqubeHostUrl = PropertyEntry.create(propertyName = "sonar.host.url", environmentName = "SONAR_HOST_URL")
    val sonarqubeLogin = PropertyEntry.create(propertyName = "sonar.login", environmentName = "SONAR_LOGIN")
    val sonarqubePassword = PropertyEntry.create(propertyName = "sonar.password", environmentName = "SONAR_PASSWORD")

    val codeCoverageMinimum = PropertyEntry.create(propertyName = "code.coverage.min", environmentName = "CODE_COVERAGE_MIN")

    val version = PropertyEntry.create(propertyName = "version", environmentName = "VERSION")
}
