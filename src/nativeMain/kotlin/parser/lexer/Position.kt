package parser.lexer

data class Position(val filename: String, val line: Int, val pos: Int) {

    override fun toString(): String {
        return "$line:$pos"
    }
}
