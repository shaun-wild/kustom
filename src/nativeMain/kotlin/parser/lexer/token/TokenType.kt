package parser.lexer.token

enum class TokenType(
    /**
     * A string to direct match against for this token.
     * */
    val match: String? = null,

    /**
     * A regular expression to match this token type against.
     * */
    pattern: String = "",

    /** An irrelevant token type will be ignored if there are multiple token matches. */
    val irrelevant: Boolean = false,

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

    /**
     * A custom matcher for a given string.
     * */
    val customMatcher: ((String) -> Boolean)? = null,

    val relatedTypes: Array<ObjType> = arrayOf()
) {
    INT(pattern = "\\d+", mapper = String::toInt),
    FLOAT(pattern = "(?:\\d*?\\d+[Ff]|\\d*\\.\\d*[Ff]?)", mapper = String::toFloat),
    STRING(pattern = "(?:\".*[^\\\\]\"|\"\")", copyValue = true, mapper = { it.subSequence(1, it.length - 1) }),
    COMMENT(pattern = "\\/\\/.*", discard = true),
    BLOCK_COMMENT(
        customMatcher = { (it == "/" || it.startsWith("/*")) && (it.endsWith("*/") || !it.contains("*/")) },
        irrelevant = true,
        discard = true
    ),
    TRUE("true", mapper = String::toBoolean), FALSE("false", mapper = String::toBoolean),
    PLUS("+", relatedTypes = arrayOf(ObjType.NUMBER, ObjType.STRING)),
    MINUS("-", relatedTypes = arrayOf(ObjType.NUMBER)),
    MULTIPLY("*", relatedTypes = arrayOf(ObjType.NUMBER)),
    DIVIDE("/", relatedTypes = arrayOf(ObjType.NUMBER)),
    MODULUS("%", relatedTypes = arrayOf(ObjType.NUMBER)),
    ASSIGN("="),
    LESS_THAN("<", relatedTypes = arrayOf(ObjType.NUMBER)),
    GREATER_THAN(">", relatedTypes = arrayOf(ObjType.NUMBER)),
    GT_EQUAL(">=", relatedTypes = arrayOf(ObjType.NUMBER)),
    LT_EQUAL("<=", relatedTypes = arrayOf(ObjType.NUMBER)),
    EQUALS("=="), NOT_EQUAL("!="),
    AND("&&", relatedTypes = arrayOf(ObjType.BOOLEAN)),
    OR("||", relatedTypes = arrayOf(ObjType.BOOLEAN)),
    VAR("var"), VAL("val"),
    LPAREN("("), RPAREN(")"),
    LBRACE("{"), RBRACE("}"),
    COMMA(","),
    ELVIS("?:"),
    PIPELINE("|>"),
    FUNCTION("fun"),
    IF("if"),
    ELSE("else"),
    NULL("null"),
    IDENTIFIER(
        pattern = "[A-z][A-z0-9_]*",
        copyValue = true, irrelevant = true
    ),
    NEWLINE(pattern = "\n"), EOF;

    val regex = (if (match != null) Regex.escape(match) else pattern).toRegex()

    /**
     * @return true if the current token may match the given string.
     * */
    fun matches(text: String) = customMatcher?.invoke(text) ?: regex.matches(text)

    /**
     * @return true if the given text is this token.
     * */
    fun valid(text: String) = regex.matches(text)

    override fun toString(): String {
        if (match != null) {
            return match
        }

        return name
    }
}
