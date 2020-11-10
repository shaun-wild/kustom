package parser.parser.nodes

import parser.lexer.token.Token

class NumberNode(parent: Node?, start: Token, val number: Number): Node(parent, start) {

    override fun visit()= number
    override fun toString()= "$number"
}
