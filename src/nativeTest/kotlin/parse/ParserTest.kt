package parse

import assertToString
import parseText
import parser.ParseException
import kotlin.test.Test
import kotlin.test.assertFailsWith

class ParserTest {

    @Test
    fun `parse basic expression`() {
        val input = "1 + 1"
        val output = parseText(input)

        assertToString("(1 + 1)", output)
    }

    @Test
    fun `parse longer expression`() {
        val input = "1 + 1 + 5"
        val output = parseText(input)

        assertToString("((1 + 1) + 5)", output)
    }

    @Test
    fun `parser exception for extraneous identifier`() {
        val input = "500lol"
        assertFailsWith<ParseException> { parseText(input) }
    }

    @Test
    fun `parse order of operations`() {
        val input = "5 + 2 * 4"
        val output = parseText(input)

        assertToString("(5 + (2 * 4))", output)
    }

    @Test
    fun `String is parsed`() {
        val input = "\"test\""
        val output = parseText(input)

        assertToString("test", output)
    }

    @Test
    fun `Float multiplication`() {
        val input = "5F * 5F"
        val output = parseText(input)

        assertToString("(5.0 * 5.0)", output)
    }

    @Test
    fun `Simple boolean`() {
        val input = "true"
        val output = parseText(input)

        assertToString("true", output)
    }

    @Test
    fun `Parenthesis change order of operations`() {
        val input = "5 * (1 + 3)"
        val output = parseText(input)

        assertToString("(5 * (1 + 3))", output)
    }

    @Test
    fun `body parse`() {
        val input = """
            {
                25 + 2
                "hello"
            }
        """.trimIndent()
        val output = parseText(input)

        assertToString("{(25 + 2) hello}", output)
    }
}
