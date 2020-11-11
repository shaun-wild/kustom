package parser.parser.nodes

import parser.lexer.token.Token

class ObjectNode(start: Token, val value: Any?): Node(start) {

    override fun visit()= value
    override fun toString()= "$value"
}
