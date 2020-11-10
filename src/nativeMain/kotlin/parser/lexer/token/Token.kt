package parser.lexer.token

import parser.lexer.Position

data class Token(val name: TokenType, val value: Any? = null, val start: Position, val end: Position) {
    override fun toString(): String {
        if(value != null) {
            return "$name ($value)"
        } else {
            return "$name"
        }
    }

    override fun equals(other: Any?): Boolean {
        if(other is Token) {
            return this.name == other.name && this.value == other.value
        }

        return false
    }
}
