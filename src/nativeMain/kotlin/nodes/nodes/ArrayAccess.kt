package nodes.nodes

import interpreter.Context
import parser.ParseException
import parser.lexer.token.Token

class ArrayAccess(start: Token, end: Token, val left: Node, val index: Node) : Node(start, end) {
    override fun visit(context: Context): Any? {
        val array = left.visit(context)

        if(array is List<Any?>) {
            val accessor = index.visit(context)

            if(accessor is Int) {
                return array[accessor]
            } else {
                throw ParseException("Expected int", index.start, index.end)
            }
        } else {
            throw ParseException("Expected array", left.start, left.end)
        }
    }

    override fun toString()= "[$index]"
}
