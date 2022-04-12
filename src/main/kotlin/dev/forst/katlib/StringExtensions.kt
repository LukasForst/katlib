package dev.forst.katlib

import java.net.URL
import java.util.UUID
import java.util.regex.Pattern

/**
 * Shortens the string to [maxLength]; in such case, appends the [ellipsis] (typically "…" ).
 *
 * For example, returns "ABCD…" when called for "ABCDEFHG".(5, "…")
 */
fun String.restrictLengthWithEllipsis(maxLength: Int, ellipsis: String = "…"): String =
    if (this.length <= maxLength) {
        this
    } else {
        this.substring(0, maxLength - ellipsis.length) + ellipsis
    }

private val regexStartsWithLetter = "^[a-zA-Z]".toRegex()

/**
 * Returns true if the string starts with a latin letter a-z or A-Z.
 */
fun String.startsWithLetter() = this.contains(regexStartsWithLetter)

/**
 * Converts given string to UUID.
 */
fun String.toUuid(): UUID = UUID.fromString(this)

/**
 * See documentation for [isUuid].
 */
fun String.isUuid(): Boolean = isUuid(this)

/**
 * See documentation for [isUrl].
 */
fun String.isUrl(): Boolean = isUrl(this)

/**
 * Creates [URL] instance from [this] string.
 */
fun String.toUrl(): URL = URL(this)

/**
 * Checks if string is valid email address.
 *
 * This is just a basic checking, please use Apache Validator for more complex cases.
 */
fun String.isEmail(): Boolean = emailRegex.matcher(this).matches()

// Taken from Android/Kotlin lib, thus suppression
@Suppress("RegExpRedundantEscape", "RegExpDuplicateCharacterInClass")
private val emailRegex = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)

/**
 * Returns base64 representation of this string.
 */
fun String.toBase64(): String = this.encodeToByteArray().toBase64()

/**
 * Compares two strings ignoring case.
 */
infix fun String?.equalsIgnoreCase(other: String?): Boolean = this.equals(other, ignoreCase = true)
