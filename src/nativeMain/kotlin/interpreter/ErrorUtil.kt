package interpreter

import parser.ParseException

fun underlineError(errorText: String, error: ParseException): String {
    val startPos = error.start.pos
    val endPos = error.end.pos

    var result = "ERROR ${error.message}\n${getLine(errorText, error.start.line)}\n"

    for (i in 0..errorText.length) {
        result += if (i in startPos..endPos) "^" else " "
    }

    return result
}

private fun getLine(text: String, line: Int) = text.split("\n")[line]