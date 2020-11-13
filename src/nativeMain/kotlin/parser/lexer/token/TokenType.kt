package parser.lexer.token

import parser.lexer.token.ObjType.BOOLEAN
import parser.lexer.token.ObjType.NUMBER

/**
 * TokenType is an enum representing all of the token's and their respective matches.
 * Tokens should be ordered by specificity, from highest, to lowest. For example,
 * FLOAT is above INT because we want a successful float match to take precedence.
 * */
enum class TokenType(
    /**
     * A string to direct match against for this token.
     * */
    val match: String? = null,

    /**
     * A regular expression to match this token type against.
     * */
    pattern: String? = null,

    /**
     * Whether this token should be discarded by the parser.
     * */
    val discard: Boolean = false,

    /** An optional mapper to convert the token string into the correct form. */
    val mapper: ((String) -> Any)? = null,

    /**
     * Whether the matched token string should be copied or not. Implicitly true if a mapper is set.
     * */
    val copyValue: Boolean = (mapper != null),

    val relatedTypes: Array<ObjType> = arrayOf(),

    val keyword: Boolean = match?.let { KEYWORD_REGEX.matches(it) } ?: false
) {
    FLOAT(pattern = "\\d+[Ff]|\\d*\\.\\d+[fF]?", mapper = String::toFloat),
    INT(pattern = "\\d+", mapper = String::toInt),
    STRING(pattern = "\"(?:[^\"]|\\\\\")*[^\\\\]\"", copyValue = true, mapper = { it.subSequence(1, it.length - 1) }),
    COMMENT(pattern = "\\/\\/.*", discard = true),
    BLOCK_COMMENT(pattern = "\\/\\*.*\\*\\/", discard = true),
    TRUE("true", mapper = String::toBoolean), FALSE("false", mapper = String::toBoolean),
    PLUS("+", relatedTypes = arrayOf(NUMBER, ObjType.STRING)),
    MINUS("-", relatedTypes = arrayOf(NUMBER)),
    MULTIPLY("*", relatedTypes = arrayOf(NUMBER)),
    DIVIDE("/", relatedTypes = arrayOf(NUMBER)),
    MODULUS("%", relatedTypes = arrayOf(NUMBER)),
    GT_EQUAL(">=", relatedTypes = arrayOf(NUMBER)),
    LT_EQUAL("<=", relatedTypes = arrayOf(NUMBER)),
    EQUALS("=="),
    NOT_EQUAL("!="),
    ASSIGN("="),
    PIPELINE("->"),
    ELVIS("?:"),
    LESS_THAN("<", relatedTypes = arrayOf(NUMBER)),
    GREATER_THAN(">", relatedTypes = arrayOf(NUMBER)),
    NOT("!", relatedTypes = arrayOf(BOOLEAN)),
    AND("&&", relatedTypes = arrayOf(BOOLEAN)),
    OR("||", relatedTypes = arrayOf(BOOLEAN)),
    VAR("var", keyword = true),
    VAL("val", keyword = true),
    LPAREN("("),
    RPAREN(")"),
    LBRACE("{"),
    RBRACE("}"),
    COMMA(","),
    FUNCTION("fun"),
    IF("if"),
    ELSE("else"),
    NULL("null"),
    IDENTIFIER(
        pattern = "[A-z][A-z0-9_]*",
        copyValue = true
    ),
    NEWLINE("\n"), EOF;

    companion object {
        val KEYWORD_REGEX = "[A-z][A-z0-9_]*".toRegex()

        val REGEX = values()
            .filter { it.regex != null }
            .joinToString("|") { "(${it.regex!!.pattern})" }
            .toRegex(setOf(RegexOption.DOT_MATCHES_ALL))
    }

    val regex = if (match != null) {
        val matchRegex = Regex.escape(match)
        if (keyword) "(?<![A-z0-9_])${matchRegex}(?![A-z0-9_])" else matchRegex
    } else {
        pattern
    }?.toRegex()

    override fun toString(): String {
        if (match != null) {
            return match
        }

        return name
    }
}
