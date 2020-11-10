package parser.parser.nodes

import parser.ParseException
import parser.lexer.token.Token

abstract class Node(val parent: Node?, val start: Token, val end: Token = start) {
    abstract fun visit(): Any?

    protected fun fail(message: String)= ParseException("file", message, start, end)
}
