package nodes.nodes

import extension.unaryMinus
import interpreter.Context
import parser.ParseException
import parser.lexer.token.Token
import parser.lexer.token.TokenType

class UnaryOperation(start: Token, end: Token, val operation: TokenType, val node: Node) : Node(start, end) {

    override fun visit(context: Context): Any? {
        val value = node.visit(context)

        if (value is Number) {
            if(operation == TokenType.MINUS) {
                return -value
            }

            if(operation == TokenType.PLUS) {
                return value
            }
        }

        if(value is Boolean) {
            if(operation == TokenType.NOT) {
                return !value
            }
        }

        throw ParseException("Undefined unary operation $operation$value", start, end)
    }
}
