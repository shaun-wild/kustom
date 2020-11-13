package nodes.nodes

import interpreter.Context
import parser.lexer.token.ObjType
import parser.lexer.token.Token

class FunctionNode(start: Token, end: Token, val name: String?,  val arguments: List<String>, val body: Node) : Node(start, end) {

    override fun visit(context: Context): Any? {
        if(name != null) {
            context.setVariable(ObjType.FUNCTION, name, false, this)
        }

        return this
    }

    override fun toString()= "fun $name(${arguments.joinToString()}) $body"
}
