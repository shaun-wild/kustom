package parser

import parser.lexer.Position
import parser.lexer.token.Token

class ParseException(
    val file: String,
    message: String,
    val start: Position,
    val end: Position,
) : Exception("<$file> (${start.line}:${start.pos}-${end.line}:${end.pos}) | $message") {
    constructor(file: String, message: String, start: Token, end: Token) : this(file, message, start.start, end.end)
    constructor(message: String, start: Token, end: Token = start) : this(start.start.filename, message, start, end)
}
