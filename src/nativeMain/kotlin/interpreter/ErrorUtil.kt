package interpreter

import parser.ParseException

fun underlineError(errorText: String, error: ParseException): String {
    val startPos = error.start.pos
    val endPos = error.end.pos

    var result = "ERROR ${error.message}\n${errorText}\n"

    for (i in 0..errorText.length) {
        result += if (i in startPos..endPos) "^" else " "
    }

    return result
}
