package parser.lexer

import parser.ParseException
import parser.lexer.token.Token
import parser.lexer.token.TokenType
import parser.lexer.token.TokenType.EOF
import parser.lexer.token.TokenType.values

class Lexer(text: String, val file: String = "stdin") {

    companion object {
        val IGNORE = arrayOf(' ', '\t')
    }

    val iterator = text.toCharArray().iterator()
    var current: Char? = iterator.next()

    var pos: Position = Position(file, 0, 0)

    fun lex(): List<Token> {
        val result = mutableListOf<Token>()

        while (current != null) {
            if (current in IGNORE) {
                advance()
                continue
            }

            val next = nextToken()
            result += next
        }

        result += Token(EOF, start = pos, end = pos)

        return result
    }

    private fun advance() {
        if (iterator.hasNext()) {
            current = iterator.next()
            pos = pos.copy(pos = pos.pos + 1, line = if (current == '\n') pos.line + 1 else pos.line)
        } else {
            current = null
        }
    }

    private fun nextToken(): Token {
        var token = ""
        val start = pos.copy()

        val potentialResults = mutableListOf<Token>()

        while (current != null) {
            token += current
            val tokens = listTokens(token)

//            println("$token becomes $tokens")

            if (tokens.size == 1) {
                val (tokenType) = tokens
                potentialResults.add(processToken(tokenType, token, start, pos))
            } else if (potentialResults.isNotEmpty() && tokens.isEmpty()) {
                break
            }

            advance()
        }

        val end = pos.copy()

        if (potentialResults.isEmpty()) {
            throw ParseException(file, "Could not tokenize $token", start, end)
        } else {
            return potentialResults.last()
        }
    }

    private fun processToken(tokenType: TokenType, value: String, start: Position, end: Position): Token {
        val tokenValue = tokenType.mapper?.invoke(value) ?: value

        if (tokenType.copyValue) {
            return Token(tokenType, tokenValue, start, end)
        } else {
            return Token(tokenType, start = start, end = end)
        }
    }

    private fun listTokens(token: String): List<TokenType> {
        val tokenTypes = values()
            .filter { it.matches(token) }

        if (tokenTypes.size > 1) {
            return tokenTypes.filter { !it.irrelevant }
        }

        return tokenTypes
    }
}
