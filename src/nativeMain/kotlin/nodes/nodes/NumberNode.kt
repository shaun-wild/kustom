package nodes.nodes

import interpreter.Context
import parser.lexer.token.ObjType
import parser.lexer.token.Token

class NumberNode(start: Token, val number: Number): ObjectNode(start, number, ObjType.NUMBER) {

    override fun visit(context: Context)= number
    override fun toString()= "$number"
}
