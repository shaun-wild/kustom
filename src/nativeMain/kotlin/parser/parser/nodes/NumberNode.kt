package parser.parser.nodes

import parser.lexer.token.Token

class NumberNode(start: Token, val number: Number): Node(start) {

    override fun visit()= number
    override fun toString()= "$number"
}
