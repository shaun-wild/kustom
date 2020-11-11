package parser.parser.nodes

import parser.lexer.token.Token

class BlockNode(start: Token, end: Token, val children: List<Node>) : Node(start, end) {

    override fun visit(): Any? {
        var result: Any? = null

        for(child in children) {
            result = child.visit()
        }

        return result
    }

    override fun toString()= "{${children.joinToString(" ")}}"
}
