package nodes.nodes

import interpreter.Context
import parser.lexer.token.Token

class ArrayCreate(start: Token, end: Token, val elements: List<Node>) : Node(start, end) {
    override fun visit(context: Context): Any? {
        return elements
            .map { it.visit(context) }
            .toMutableList()
    }

    override fun toString()= "[${elements.joinToString()}]"
}
