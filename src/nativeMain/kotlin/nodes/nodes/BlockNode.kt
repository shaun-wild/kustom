package nodes.nodes

import interpreter.Context
import parser.lexer.token.Token

class BlockNode(start: Token, end: Token, val children: List<Node>) : Node(start, end) {

    override fun visit(context: Context): Any? {
        var result: Any? = null

        for(child in children) {
            result = child.visit(context)
        }

        return result
    }

    override fun toString()= "{${children.joinToString(" ")}}"
}
