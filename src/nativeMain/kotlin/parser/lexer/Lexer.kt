package parser.lexer

import parser.lexer.token.Token
import parser.lexer.token.TokenType

class Lexer(val text: String, val file: String = "stdin") {

    fun lex(): List<Token> {
        val result = TokenType.REGEX.findAll(text)
            .flatMap {
                it.groups.mapIndexed { i, match ->
                    if (i > 0) {
                        if (match != null) {
                            val tokenType = TokenType.values()[i - 1]
                            val value = if (tokenType.copyValue) match.value else null
                            matchToToken(tokenType, value, match.range)
                        } else null
                    } else null
                }.filterNotNull()
            }.toMutableList()

        result += Token(TokenType.EOF, null, indexToPosition(text.length), indexToPosition(text.length))

        return result
    }

    private fun matchToToken(tokenType: TokenType, value: String?, range: IntRange): Token {
        val tokenValue = if (tokenType.copyValue) {
            if(value != null) {
                tokenType.mapper?.invoke(value) ?: value
            } else value
        } else null

        return Token(
            tokenType,
            tokenValue,
            indexToPosition(range.first),
            indexToPosition(range.last)
        )
    }

    private fun indexToPosition(index: Int): Position {
        var line = 0
        var pos = 0

        for(i in 0 until index) {
            pos++
            if(text[i] == '\n') {
                pos = 0
                line++
            }
        }

        return Position(file, line, pos)
    }
}
