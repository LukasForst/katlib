package ai.blindspot.ktoolz.extensions

import java.io.File
import java.security.MessageDigest
import java.util.Base64

/**
 * Creates SHA256 hash of the given text.
 */
fun hashWith256(textToHash: String): String = hashWith256(textToHash.toByteArray(Charsets.UTF_8))

/**
 * Creates SHA256 hash of the given file.
 */
fun hashWith256(fileToHash: File): String = hashWith256(fileToHash.readBytes())

/**
 * Creates SHA256 hash of the given byte array.
 */
fun hashWith256(bytes: ByteArray): String {
    val hashedArray = MessageDigest
        .getInstance("SHA-256")
        .digest(bytes)
    return Base64.getEncoder().encodeToString(hashedArray)
}
