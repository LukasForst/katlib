/**
 * The properties needed by build and read from `ktoolz.properties` file or environment variables.
 */
object Props {
    val bintrayUser = PropertyEntry.create(propertyName = "bintray.user", environmentName = "BINTRAY_USER")
    val bintrayApiKey = PropertyEntry.create(propertyName = "bintray.apiKey", environmentName = "BINTRAY_API_KEY")

    val sonarqubeHostUrl = PropertyEntry.create(propertyName = "sonar.host.url", environmentName = "SONAR_HOST_URL")
    val sonarqubeLogin = PropertyEntry.create(propertyName = "sonar.login", environmentName = "SONAR_LOGIN")
    val sonarqubePassword = PropertyEntry.create(propertyName = "sonar.password", environmentName = "SONAR_PASSWORD")

    val version = PropertyEntry.create(propertyName = "version", environmentName = "VERSION")
}
