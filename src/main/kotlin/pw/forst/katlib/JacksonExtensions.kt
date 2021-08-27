package pw.forst.katlib

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.util.logging.Logger

/**
 * Logger for this file.
 */
@PublishedApi
internal val jsonLogger = Logger.getLogger("pw.forst.katlib.JacksonExtension")

/**
 * Standard [ObjectMapper] configured in a way the platform operates.
 */
fun jacksonMapper(): ObjectMapper = jacksonObjectMapper().apply {
    configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
    configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false)
}

/**
 * Tries to create instance of T from provided [json], null is returned when it is not possible to parse it.
 * If [logParserException] set to true and exception is raised during JSON parsing, it is logged.
 */
inline fun <reified T> parseJson(json: String, logParserException: Boolean = true): T? =
    runCatching { jacksonMapper().readValue<T>(json) }
        .getOrLog(json, logParserException)

/**
 * Tries to create instance of T from provided [json], null is returned when it is not possible to parse it.
 * If [logParserException] set to true and exception is raised during JSON parsing, it is logged.
 */
inline fun <reified T> parseJson(json: ByteArray, logParserException: Boolean = true): T? =
    runCatching { jacksonMapper().readValue<T>(json) }
        .getOrLog(json.decodeToString(), logParserException)

@PublishedApi
internal fun <R> Result<R>.getOrLog(json: String, logParserException: Boolean) =
    onFailure {
        if (logParserException) {
            jsonLogger.warning("Exception raised during JSON parsing:$newLine${it.message}$newLine$json$newLine")
        }
    }.getOrNull()

/**
 * Serializes given object to string.
 */
fun <T : Any> createJson(value: T): String = jacksonMapper().writeValueAsString(value)

/**
 * Serializes given object to string.
 */
fun <T : Any> createPrettyJson(value: T): String = jacksonMapper()
    .writerWithDefaultPrettyPrinter()
    .writeValueAsString(value)


/**
 * Serializes given object to byte array.
 */
fun <T : Any> createJsonBytes(value: T): ByteArray = jacksonMapper().writeValueAsBytes(value)

/**
 * Pretty print a json.
 */
fun prettyPrintJson(json: String): String = with(jacksonMapper()) {
    writerWithDefaultPrettyPrinter().writeValueAsString(readValue<Any>(json))
}
