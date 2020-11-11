package parser.parser.nodes

import extension.*
import parser.lexer.token.Token
import parser.lexer.token.TokenType.*

class BinaryOperation(val left: Node, val operation: Token, val right: Node) :
    Node(left.start, right.end) {
    override fun visit(): Any? {
        val left = left.visit()
        val right = right.visit()

        if (operation.name == EQUALS) {
            return left == right
        } else if (operation.name == NOT_EQUAL) {
            return left != right
        }

        if (left is Number && right is Number) {
            return when (operation.name) {
                PLUS -> left + right
                MINUS -> left - right
                MULTIPLY -> left * right
                DIVIDE -> left / right
                LESS_THAN -> left < right
                GREATER_THAN -> left > right
                LT_EQUAL -> left <= right
                GT_EQUAL -> left >= right
                else -> throw fail("Undefined operation $left ${operation.name} $right")
            }
        }

        if (left is Boolean && right is Boolean) {
            return when (operation.name) {
                AND -> left and right
                OR -> left or right
                else -> throw fail("Undefined operation $left ${operation.name} $right")
            }
        }


        if (operation.name == PLUS) {
            if (left is String) {
                return left + right
            }
        }

        throw fail("Undefined operation $left ${operation.name} $right")
    }

    override fun toString() = "($left ${operation.name} $right)"
}
