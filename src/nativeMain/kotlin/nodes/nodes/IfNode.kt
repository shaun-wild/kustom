package nodes.nodes

import interpreter.Context
import parser.lexer.token.Token

class IfNode(start: Token, end: Token, val condition: Node, val ifTrue: Node, val ifFalse: Node?) : Node(start, end) {

    override fun visit(context: Context): Any? {
        val condition = condition.visit(context) as Boolean

        if (condition) {
            return ifTrue.visit(context)
        } else {
            return ifFalse?.visit(context)
        }
    }

    override fun toString() = "if($condition) $ifTrue" + if (ifFalse != null) " else $ifFalse" else ""
}
