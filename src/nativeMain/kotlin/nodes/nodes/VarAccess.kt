package nodes.nodes

import interpreter.Context
import parser.lexer.token.ObjType
import parser.lexer.token.Token
import parser.lexer.token.TokenType
import parser.lexer.token.TokenType.*

class VarAccess(val identifier: Token) : Node(identifier) {

    companion object {
        val ASSIGN_TYPES = arrayOf(ASSIGN, PLUS_ASSIGN)
    }

    val varName = identifier.value as String

    override fun visit(context: Context): Any? {
        if(isSetter()) {
            return identifier
        }

        val type = getType()
        return context.getVariable(type, varName)
    }

    override fun toString(): String {
        return "${if(isSetter()) "set" else "get"} $identifier"
    }

    private fun isSetter(): Boolean {
        return (parent as? BinaryOperation)?.operation?.name in ASSIGN_TYPES
    }

    private fun getType(): ObjType {
        val parent = this.parent

        if(parent is BinaryOperation) {
            val other = if(parent.left == this) parent.right else parent.left

            if(other is ObjectNode) {
                return other.type
            }
        }

        return ObjType.ANY
    }
}
