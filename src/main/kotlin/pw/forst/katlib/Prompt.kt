package pw.forst.katlib

/**
 * Prompts user with @param [promptText] and applies @param [transform] to it.
 * If @param [transform] throws an exception on user input, @param [exceptionHandler] will be invoked,
 * and prompt will be repeated.
 *
 * Example: snippet below will ask user for input until given input can be parsed to Double
 * ```
 * prompt(
 * 	"input number:",
 * 	{ "this is not a number" },
 * )
 * { it.toDouble() }
 * ```
 * @return user input with transform applied
 */
fun <R> prompt(promptText: String, exceptionHandler: (e: Exception) -> String? = { null }, transform: (input: String) -> R): R {
	while (true) {
		print(promptText)
		try {
			return transform(readln())
		} catch (e: Exception) {
			exceptionHandler(e)?.let { println(it) }
		}
	}
}