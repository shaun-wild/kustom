package parse

import parser.ParseException
import parser.lexer.Lexer
import parser.lexer.token.TokenType
import parser.lexer.token.TokenType.*
import token
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LexerTest {

    companion object {
        val EOF = token(TokenType.EOF)
    }

    @Test
    fun `lex integer`() {
        val input = "125"
        val output = Lexer(input)
            .lex()

        assertEquals(listOf(token(INT, 125), EOF), output)
    }

    @Test
    fun `lex single character`() {
        val input = "+"
        val output = Lexer(input)
            .lex()

        assertEquals(listOf(token(PLUS), EOF), output)
    }

    @Test
    fun `lex expression`() {
        val input = "5 + 3"
        val output = Lexer(input)
            .lex()

        assertEquals(listOf(token(INT, 5), token(PLUS), token(INT, 3), EOF), output)
    }

    @Test
    fun `lex division`() {
        val input = "100 / 5"
        val output = Lexer(input)
            .lex()

        assertEquals(listOf(token(INT, 100), token(DIVIDE), token(INT, 5), EOF), output)
    }

    @Test
    fun `lex string`() {
        val input = "\"test\""
        val output = Lexer(input)
            .lex()

        assertEquals(listOf(token(STRING, "test"), EOF), output)
    }

    @Test
    fun `lex string long`() {
        val input = "\"this is \\\"a\\\" long string\""
        val output = Lexer(input)
            .lex()

        assertEquals(listOf(token(STRING, "this is \\\"a\\\" long string"), EOF), output)
    }

    @Test
    fun `lex identifier`() {
        val input = "a=5"
        val output = Lexer(input)
            .lex()

        assertEquals(listOf(token(IDENTIFIER, "a"), token(ASSIGN), token(INT, 5), EOF), output)
    }

    @Test
    fun `lex keywords`() {
        val input = "var a = 5"
        val output = Lexer(input)
            .lex()

        assertEquals(listOf(token(VAR), token(IDENTIFIER, "a"), token(ASSIGN), token(INT, 5), EOF), output)
    }

    @Test
    fun `lex int and identifier`() {
        val input = "500lol"
        val output = Lexer(input)
            .lex()

        assertEquals(listOf(token(INT, 500), token(IDENTIFIER, "lol"), EOF), output)
    }

    @Test
    fun `lex float with suffix`() {
        val input = "200F"
        val output = Lexer(input)
            .lex()

        assertEquals(listOf(token(FLOAT, 200F), EOF), output)
    }

    @Test
    fun `lex float with floating point`() {
        val input = "50.0F"
        val output = Lexer(input)
            .lex()

        assertEquals(listOf(token(FLOAT, 50.0F), EOF), output)
    }

    @Test
    fun `lex parenthesis`() {
        val input = "5 * (1 + 3)"
        val output = Lexer(input)
            .lex()

        assertEquals(
            listOf(
                token(INT, 5), token(MULTIPLY), token(LPAREN), token(INT, 1), token(PLUS),
                token(INT, 3), token(RPAREN), EOF
            ), output
        )
    }

    @Test
    fun `comment after expression`() {
        val input = "2 + 2 // This is a comment"
        val output = Lexer(input)
            .lex()

        assertEquals(
            listOf(token(INT, 2), token(PLUS), token(INT, 2), token(COMMENT), EOF), output
        )
    }

    @Test
    fun `block comment`() {
        val input = "/* test */"
        val output = Lexer(input)
            .lex()

        assertEquals(
            listOf(token(BLOCK_COMMENT), EOF), output
        )
    }

    @Test
    fun `block comment expression`() {
        val input = "5 + /* another number */ 2"
        val output = Lexer(input)
            .lex()

        assertEquals(
            listOf(token(INT, 5), token(PLUS), token(BLOCK_COMMENT), token(INT, 2), EOF), output
        )
    }

    @Test
    fun `unary minus identifier`() {
        val input = "-a"
        val output = Lexer(input)
            .lex()

        assertEquals(listOf(token(MINUS), token(IDENTIFIER, "a"), EOF), output)
    }

    @Test
    fun `lex newline`() {
        val input = """
            a
            b
        """.trimIndent()
        val output = Lexer(input)
            .lex()

        assertEquals(listOf(token(IDENTIFIER, "a"), token(NEWLINE), token(IDENTIFIER, "b"), EOF), output)
    }

//    @Test
//    fun `error on invalid token`() {
//        val input = "@"
//        assertFailsWith<ParseException> { Lexer(input).lex() }
//    }
}
