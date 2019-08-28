import java.io.File
import java.util.Properties

/**
 * A property read from an environment variable or from `.env` file in this order. They are used solely for configuring gradle builds. The values are first
 * read from environment variables using a given environment variable name or secondly from a property file using a given property name in the file. The
 * property file is `ktoolz.properties` in the project root.
 */
class PropertyEntry(
    val properties: Properties,
    val propertyName: String,
    val environmentName: String
) {
    companion object {
        private val propsFile = File(System.getenv("PROJECT_ROOT") ?: System.getProperty("user.dir"), "ktoolz.properties")
        private val properties = Properties()

        init {
            if (propsFile.exists()) {
                properties.load(propsFile.bufferedReader())
            }
        }

        /**
         * The constructor of [PropertyEntry].
         *
         * @param propertyName Name of the property in the property file.
         * @param environmentName Name of the property, if taken from environment variable.
         */
        internal fun create(propertyName: String, environmentName: String) =
            PropertyEntry(
                properties = properties,
                propertyName = propertyName,
                environmentName = environmentName
            )
    }

    /**
     * Fetches the property value, if it is not found among env vars or in property file, <code>null</code> is returned.
     */
    fun getOrNull(): String? = System.getenv(environmentName) ?: properties.getProperty(propertyName)

    /**
     * Fetches the property value, if it is not found among env vars or in property file, <code>default</code> is returned.
     *
     * @param default Default value if it is not found.
     */
    fun getOrDefault(default: String = ""): String = getOrNull() ?: default

    /**
     * Fetches the property value.
     *
     * @throws PropertyNotFoundException If the property is not defined.
     */
    fun get(): String = getOrNull()
        ?: throw PropertyNotFoundException(
            "Neither '$environmentName' was found within environment variables nor '$propertyName' was found in local properties file."
        )
}

/**
 * Thrown if the property can not be read, either from `.env` or environment variables.
 */
class PropertyNotFoundException(message: String) : Exception(message)
