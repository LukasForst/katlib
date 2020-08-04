package pw.forst.tools.katlib

import java.net.URL
import java.util.UUID

/**
 * Check whether a given string is a valid UUID.
 *
 * @param candidateUUID A candidate UUID to be checked.
 * @return true iff [candidateUUID] is a valid UUID.
 */
fun isUUID(candidateUUID: String): Boolean = runCatching { UUID.fromString(candidateUUID) }.isSuccess

/**
 * Check whether a given string is a valid URL.
 *
 * Please note that this function is not all mighty and it only tries to convert the given string to URL and then to URI.
 * This means that it fails to recognize invalid urls such as  `https://sm.ai,` or `https://`.
 * For more complex validation, one should probably use Apache URL Validator.
 *
 * For the sample cases when this simple method fails please see `isUrlFalsePositives` test in the file PureFunctionsKtTest.kt.
 *
 * @param candidateUrl A candidate URL to be checked.
 * @return true iff [candidateUrl] is a valid URL.
 */
fun isURL(candidateUrl: String): Boolean = runCatching { URL(candidateUrl).toURI() }.isSuccess
