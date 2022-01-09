package pw.forst.katlib

import java.io.File
import java.security.MessageDigest
import java.util.Base64

/**
 * Creates SHA256 hash of the given text.
 */
fun hashWithSha256(textToHash: String): String = hashWithSha256(textToHash.toByteArray(Charsets.UTF_8))

/**
 * Creates SHA256 hash of the given file.
 */
fun hashWithSha256(fileToHash: File): String = hashWithSha256(fileToHash.readBytes())

/**
 * Creates SHA256 hash of the given byte array.
 */
fun hashWithSha256(bytes: ByteArray): String = MessageDigest
    .getInstance("SHA-256")
    .digest(bytes)
    .toBase64()

/**
 * Returns base64 encoded SHA256 of given byte array.
 */
fun ByteArray.sha256(): String = hashWithSha256(this)

/**
 * Computes MD5 from given byte array, returns base64 encoded data.
 */
fun computeMd5(bytes: ByteArray): String = MessageDigest.getInstance("MD5")
    .apply { update(bytes, 0, bytes.size) }
    .digest()
    .toBase64()

/**
 * Returns base64 string MD5 of given byte array.
 */
fun ByteArray.md5(): String = computeMd5(this)

/**
 * Returns base64 string representation of given byte array.
 */
fun ByteArray.toBase64(): String = Base64.getEncoder().encodeToString(this)
