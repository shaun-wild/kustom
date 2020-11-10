package parser.parser.nodes

import parser.lexer.token.Token

class ObjectNode(parent: Node?, start: Token, val value: Any?): Node(parent, start) {

    override fun visit()= value
    override fun toString()= "$value"
}
