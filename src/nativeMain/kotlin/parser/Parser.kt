package parser

import nodes.nodes.*
import parser.lexer.token.ObjType
import parser.lexer.token.Token
import parser.lexer.token.TokenType
import parser.lexer.token.TokenType.*

class Parser(tokens: List<Token>) {

    companion object {
        /** In order of priority, Low -> High */
        val OPERATORS = arrayOf(
            arrayOf(ASSIGN, PLUS_ASSIGN, ELVIS),
            arrayOf(EQUALS, NOT_EQUAL),
            arrayOf(AND, OR),
            arrayOf(LESS_THAN, GREATER_THAN, LT_EQUAL, GT_EQUAL),
            arrayOf(PLUS, MINUS),
            arrayOf(MULTIPLY, DIVIDE, MODULUS)
        )

        val UNARY_OPERATORS = arrayOf(
            PLUS, MINUS, NOT
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

        while (iterator.hasNext()) {
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

        if(current.name == LPAREN) {
            left = parseFunctionCall(left)
        }

        if(current.name == LSQUARE) {
            left = parseArrayAccess(left)
        }

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

        if (token.name == IDENTIFIER) {
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

        if (token.name in UNARY_OPERATORS) {
            advance()
            return UnaryOperation(token, current, token.name, parse())
        }

        if (token.name == FUNCTION) {
            return parseFunction()
        }

        if(token.name == LSQUARE) {
            return parseArrayNode()
        }

        throw ParseException("Unexpected token $token", token)
    }

    private fun parseArrayAccess(node: Node): Node {
        val start = current
        advance()
        val index = parse()
        require(RSQUARE)
        return ArrayAccess(start, current, node, index)
    }

    private fun parseArrayNode(): Node {
        val start = current
        advance()
        val elements = requireUntil(RSQUARE) {
            val element = parse()
            optional(COMMA)
            element
        }

        return ArrayCreate(start, current, elements)
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

    private fun parseFunction(): Node {
        val start = current
        advance()
        val functionName = require(IDENTIFIER).value as String
        require(LPAREN)

        val arguments = requireUntil(RPAREN) {
            val arg = require(IDENTIFIER).value as String
            optional(COMMA)
            arg
        }

        val body = parse()

        return FunctionNode(start, current, functionName, arguments, body)
    }

    fun parseFunctionCall(function: Node): Node {
        val start = current
        advance()
        val arguments = requireUntil(RPAREN) {
            val arg = parse()
            optional(COMMA)
            arg
        }

        return FunctionCall(start, function, arguments)
    }

    private fun require(vararg tokenType: TokenType): Token {
        if (current.name in tokenType) {
            val result = current
            advance()
            return result
        }

        throw ParseException("Expected ${tokenType.joinToString()}, got ${current.name}", current)
    }

    private fun optional(vararg tokenType: TokenType): Token? {
        if (current.name in tokenType) {
            val result = current
            advance()
            return result
        }

        return null
    }

    private fun skipNewlines() {
        while (current.name == NEWLINE) {
            advance()
        }
    }

    private fun <T> requireUntil(tokenType: TokenType, action: (Int) -> (T)): List<T> {
        val result = mutableListOf<T>()


        var i = 0
        while (current.name != tokenType) {
            result.add(action.invoke(i++))

            if (current.name == EOF) {
                throw ParseException("Encountered EOF while parsing for $tokenType", current)
            }
        }

        advance()

        return result
    }
}
