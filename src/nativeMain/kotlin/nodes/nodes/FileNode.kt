package nodes.nodes

import interpreter.Context
import parser.lexer.token.Token

class FileNode(start: Token, end: Token, private val children: List<Node>) : Node(start, end) {

    init {
        children.forEach { it.parent = this }
    }

    override fun visit(context: Context): Any? {
        var result: Any? = null

        for(child in children) {
            result = child.visit(context)
        }

        return result
    }

    override fun toString()= children.joinToString("\n")
}
