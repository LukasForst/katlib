package pw.forst.tools.katlib

/**
 * Shortens the string to [maxLength]; in such case, appends the [ellipsis] (typically "…" ).
 *
 * For example, returns "ABCD…" when called for "ABCDEFHG".(5, "…")
 */
fun String.restrictLengthWithEllipsis(maxLength: Int, ellipsis: String = "…"): String =
    if (this.length <= maxLength) this
    else this.substring(0, maxLength - ellipsis.length) + ellipsis

/**
 * Returns true if the string starts with a latin letter a-z or A-Z
 */
fun String.startsWithLetter() = this.contains(regexStartsWithLetter)

private val regexStartsWithLetter = "^[a-zA-Z]".toRegex()
