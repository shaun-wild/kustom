package nodes.nodes

import extension.*
import interpreter.Context
import parser.lexer.token.ObjType
import parser.lexer.token.Token
import parser.lexer.token.TokenType.*

class BinaryOperation(val left: Node, val operation: Token, val right: Node) :
    Node(left.start, right.end) {

    init {
        left.parent = this
        right.parent = this
    }

    override fun visit(context: Context): Any? {
        val left = left.visit(context)
        val right = right.visit(context)

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
                MODULUS -> left % right
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

        if (left is Token) {
            if (left.name == IDENTIFIER) {
                val varName = left.value as String

                when(operation.name) {
                    ASSIGN -> {
                        if (right != null) {
                            return context.setVariable(ObjType.getType(right::class), varName, true, right)
                        }
                    }
                    PLUS_ASSIGN -> return context.modifyVariable(ObjType.NUMBER, varName) {it as Number + 1}
                }
            }
        }


        if (operation.name == PLUS) {
            if (left is String) {
                return left + right
            }
        }

        if(operation.name == ELVIS) {
            return left ?: right
        }

        throw fail("Undefined operation $left ${operation.name} $right")
    }

    override fun toString() = "($left ${operation.name} $right)"
}
