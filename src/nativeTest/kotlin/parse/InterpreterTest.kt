package parse

import interpretText
import kotlin.test.Test
import kotlin.test.assertEquals

class InterpreterTest {

    @Test
    fun `interpret simple expression`() {
        val input = "5 + 5"
        val output = interpretText(input)
        assertEquals(10, output)
    }

    @Test
    fun `greater than`() {
        val input = "5 > 2"
        val output = interpretText(input)
        assertEquals(true, output)
    }

    @Test
    fun `less than`() {
        val input = "5 < 2"
        val output = interpretText(input)
        assertEquals(false, output)
    }

    @Test
    fun `interpret string concat`() {
        val input = "\"a\" + \"b\""
        val output = interpretText(input)
        assertEquals("ab", output)
    }

    @Test
    fun `interpret string to other`() {
        val input = "\"a\" + 25"
        val output = interpretText(input)
        assertEquals("a25", output)
    }

    @Test
    fun `interpret string to multiple`() {
        val input = "\"a\" + false + \"test\""
        val output = interpretText(input)
        assertEquals("afalsetest", output)
    }

    @Test
    fun `Boolean algebra`() {
        val input = "true && false"
        val output = interpretText(input)
        assertEquals(false, output)
    }

    @Test
    fun `Boolean algebra 2`() {
        val input = "true || false"
        val output = interpretText(input)
        assertEquals(true, output)
    }

    @Test
    fun `Boolean order of operations`() {
        val input = "5 > 2 && 2 < 4"
        val output = interpretText(input)
        assertEquals(true, output)
    }

    @Test
    fun `greater than or equal`() {
        val input = "2 >= 2"
        val output = interpretText(input)
        assertEquals(true, output)
    }

    @Test
    fun `less than or equal`() {
        val input = "4 <= 2"
        val output = interpretText(input)
        assertEquals(false, output)
    }

    @Test
    fun `int equality`() {
        val input = "5 == 5"
        val output = interpretText(input)
        assertEquals(true, output)
    }

    @Test
    fun `string equality`() {
        val input = "\"test\" == \"test\""
        val output = interpretText(input)
        assertEquals(true, output)
    }

    @Test
    fun `int inequality`() {
        val input = "15 != 5"
        val output = interpretText(input)
        assertEquals(true, output)
    }

    @Test
    fun `int inequality false`() {
        val input = "15 != 15"
        val output = interpretText(input)
        assertEquals(false, output)
    }

    @Test
    fun `evaluate block`() {
        val input = """
            25 + {
                500
                2500
            }
        """.trimIndent()
        val output = interpretText(input)
        assertEquals(2525, output)
    }

    @Test
    fun `comments are discarded`() {
        val input = """
            25 + 2 //Ignored comment
        """.trimIndent()
        val output = interpretText(input)
        assertEquals(27, output)
    }

    @Test
    fun `multiline comment`() {
        val input = """
            /* This is a multiline 
            comment */ 5 + 5
        """.trimIndent()
        val output = interpretText(input)
        assertEquals(10, output)
    }
}
