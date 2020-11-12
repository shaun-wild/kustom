package parser

import parser.lexer.token.Token
import parser.lexer.token.TokenType
import parser.lexer.token.TokenType.*
import nodes.nodes.*
import parser.lexer.token.ObjType

class Parser(tokens: List<Token>) {

    companion object {
        /** In order of priority, Low -> High */
        val OPERATORS = arrayOf(
            arrayOf(ASSIGN),
            arrayOf(EQUALS, NOT_EQUAL),
            arrayOf(AND, OR),
            arrayOf(LESS_THAN, GREATER_THAN, LT_EQUAL, GT_EQUAL),
            arrayOf(PLUS, MINUS),
            arrayOf(MULTIPLY, DIVIDE)
        )

        val UNARY_OPERATORS = arrayOf(
            PLUS, MINUS
        )
    }

    val iterator = tokens.iterator()
    private lateinit var current: Token

    init {
        advance()
    }

    fun parseNodes(): Node {
        val start = current
        val children = mutableListOf<Node>()

        while(iterator.hasNext()) {
            val result = parse()
            children.add(result)
        }

        if (current.name != EOF) {
            throw ParseException("Unexpected extraneous token: $current", current)
        }

        return FileNode(start, current, children)
    }

    private fun advance() {
        if (iterator.hasNext()) {
            current = iterator.next()
        }

        if (current.name.discard) {
            advance()
        }
    }

    private fun parse(level: Int = 0): Node {
        if (level >= OPERATORS.size) {
            return factor()
        }

        var left = parse(level + 1)

        while (current.name in OPERATORS[level]) {
            val operation = current
            advance()

            val right = parse(level + 1)
            left = BinaryOperation(left, operation, right)
        }

        return left
    }

    fun factor(): Node {
        skipNewlines()

        val token = current

        if (token.name == LBRACE) {
            advance()

            val nodes = mutableListOf<Node>()

            while (current.name != RBRACE) {
                val node = parse()
                nodes.add(node)
                skipNewlines()
            }
            val end = current
            advance()

            return BlockNode(token, end, nodes)
        }

        if (token.name == INT) {
            advance()
            return NumberNode(token, token.value as Int)
        }

        if (token.name == FLOAT) {
            advance()
            return NumberNode(token, token.value as Float)
        }

        if (token.name == STRING) {
            advance()
            return ObjectNode(token, token.value as String, ObjType.STRING)
        }

        if (token.name in arrayOf(TRUE, FALSE)) {
            advance()
            return ObjectNode(token, token.value as Boolean, ObjType.BOOLEAN)
        }

        if(token.name == IDENTIFIER) {
            advance()
            return VarAccess(token)
        }

        if (token.name == LPAREN) {
            advance()
            val expression = parse()

            if (current.name == RPAREN) {
                advance()
                return expression
            }
        }

        if (token.name == IF) {
            return parseIfStatement(token)
        }

        if(token.name in UNARY_OPERATORS) {
            advance()
            return UnaryOperation(token, current, token.name, parse())
        }

        throw ParseException("Unexpected token $token", token)
    }

    private fun parseIfStatement(token: Token): Node {
        advance()
        require(LPAREN)
        val condition = parse()
        require(RPAREN)
        val trueNode = parse()
        var falseNode: Node? = null

        if (current.name == ELSE) {
            advance()
            falseNode = parse()
        }

        return IfNode(token, current, condition, trueNode, falseNode)
    }

    private fun require(tokenType: TokenType): Token {
        if (current.name == tokenType) {
            val result = current
            advance()
            return result
        }

        throw ParseException("Expected $tokenType, got ${current.name}", current)
    }

    private fun skipNewlines() {
        while(current.name == NEWLINE) {
            advance()
        }
    }
}
