package parser.parser

import parser.ParseException
import parser.lexer.token.Token
import parser.lexer.token.TokenType.*
import parser.parser.nodes.*

class Parser(tokens: List<Token>) {

    companion object {
        /** In order of priority, Low -> High */
        val OPERATORS = arrayOf(
            arrayOf(EQUALS, NOT_EQUAL),
            arrayOf(AND, OR),
            arrayOf(LESS_THAN, GREATER_THAN, LT_EQUAL, GT_EQUAL),
            arrayOf(PLUS, MINUS),
            arrayOf(MULTIPLY, DIVIDE)
        )
    }

    val iterator = tokens.iterator()
    private lateinit var current: Token

    init {
        advance()
    }

    fun parseNodes(): Node {
        val result = parse()

        if(current.name != EOF) {
            throw ParseException("Unexpected extraneous token: $current", current)
        }

        return result
    }

    private fun advance() {
        if(iterator.hasNext()) {
            current = iterator.next()
        }

        if(current.name.discard) {
            advance()
        }
    }

    private fun parse(parent: Node? = null, level: Int = 0): Node {
        if(level >= OPERATORS.size) {
            return factor()
        }

        var left = parse(parent, level + 1)

        while(current.name in OPERATORS[level]) {
            val operation = current
            advance()

            val right = parse(parent, level + 1)
            left = BinaryOperation(parent, left, operation, right)
        }

        return left
    }

    fun factor(parent: Node? = null): Node {
        val token = current

        if(token.name == LBRACE) {
            advance()

            val nodes = mutableListOf<Node>()

            while(current.name != RBRACE) {
                val node = parse(parent)
                nodes.add(node)
            }
            val end = current
            advance()

            return BlockNode(parent, token, end, nodes)
        }

        if(token.name == INT) {
            advance()
            return NumberNode(parent, token, token.value as Int)
        }

        if(token.name == FLOAT) {
            advance()
            return NumberNode(parent, token, token.value as Float)
        }

        if(token.name == STRING) {
            advance()
            return ObjectNode(parent, token, token.value as String)
        }

        if(token.name in arrayOf(TRUE, FALSE)) {
            advance()
            return ObjectNode(parent, token, token.value as Boolean)
        }

        if(token.name == LPAREN) {
            advance()
            val expression = parse(parent)

            if(current.name == RPAREN) {
                advance()
                return expression
            }
        }

        throw ParseException("Unexpected token $token", token)
    }
}
