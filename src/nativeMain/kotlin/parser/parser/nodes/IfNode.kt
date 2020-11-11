package parser.parser.nodes

import parser.lexer.token.Token

class IfNode(start: Token, end: Token, val condition: Node, val ifTrue: Node, val ifFalse: Node?) : Node(start, end) {

    override fun visit(): Any? {
        val condition = condition.visit() as Boolean

        if(condition) {
            return ifTrue.visit()
        } else {
            return ifFalse?.visit()
        }
    }
}
