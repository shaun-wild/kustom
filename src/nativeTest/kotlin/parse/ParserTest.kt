package parse

import assertToString
import parseText
import parser.ParseException
import kotlin.test.Test
import kotlin.test.assertEquals
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
    fun `block parse`() {
        val input = """
            {
                25 + 2
                "hello"
            }
        """.trimIndent()
        val output = parseText(input)

        assertToString("{(25 + 2) hello}", output)
    }

    @Test
    fun `assign variable`() {
        val input = "a = 5"
        val output = parseText(input)

        assertToString("(set IDENTIFIER (a) = 5)", output)
    }

    @Test
    fun `create function`() {
        val input = """
            fun doSomething(a) {
                a
            }
        """.trimIndent()
        val output = parseText(input)

        assertToString("fun doSomething(a) {get IDENTIFIER (a)}", output)
    }

    @Test
    fun `call function`() {
        val input = "println(\"test\")"
        val output = parseText(input)

        assertToString("call <get IDENTIFIER (println)>(test)", output)
    }

    @Test
    fun `identifier containing keyword`() {
        val input = "fun b(int, value) if(value > 0) true else false"
        val output = parseText(input)
        assertToString("fun b(int, value) if((get IDENTIFIER (value) > 0)) true else false", output)
    }

    @Test
    fun `create array`() {
        val input = "a = [1, 2, 3]"
        val output = parseText(input)
        assertToString("(set IDENTIFIER (a) = [1, 2, 3])", output)
    }
}
