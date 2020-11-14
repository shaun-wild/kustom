package nodes.nodes

import interpreter.Context
import parser.lexer.token.ObjType
import parser.lexer.token.Token

open class ObjectNode(start: Token, val value: Any?, val type: ObjType = ObjType.ANY) : Node(start) {

    override fun visit(context: Context) = value
    override fun toString() = if(value is String) "\"$value\"" else value.toString()
}
