package parse

import interpretText
import nodes.nodes.FunctionNode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InterpreterTest {

    @Test
    fun `interpret simple expression`() {
        val input = "5 + 5"
        val output = interpretText(input)
        assertEquals(10, output)
    }

    @Test
    fun `division expression`() {
        val input = "100 / 5"
        val output = interpretText(input)
        assertEquals(20, output)
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
    fun `modulus operator`() {
        val input = "10 % 3"
        val output = interpretText(input)
        assertEquals(1, output)
    }


    @Test
    fun `unary operator`() {
        val input = "-100"
        val output = interpretText(input)
        assertEquals(-100, output)
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

    @Test
    fun `if statement`() {
        val input = "if(5 > 2) 5"
        val output = interpretText(input)
        assertEquals(5, output)
    }

    @Test
    fun `if else statement`() {
        val input = "if(2 > 4) 10 else 2"
        val output = interpretText(input)
        assertEquals(2, output)
    }

    @Test
    fun `assign variable`() {
        val input = "a = 100"
        val output = interpretText(input)
        assertEquals(100, output)
    }

    @Test
    fun `assign and access variable`() {
        val input = """
            a = 20
            a + 100
        """.trimIndent()
        val output = interpretText(input)
        assertEquals(120, output)
    }

    @Test
    fun `access variable without context`() {
        val input = """
            a = 20
            a
        """.trimIndent()
        val output = interpretText(input)
        assertEquals(20, output)
    }

    @Test
    fun `unary minus`() {
        val input = """
            a = 20
            -a
        """.trimIndent()
        val output = interpretText(input)
        assertEquals(-20, output)
    }

    @Test
    fun `function declaration`() {
        val input = """
            fun test(a) {
                a * a
            }
        """.trimIndent()
        val output = interpretText(input)
        assertTrue(output is FunctionNode)
    }

    @Test
    fun `function declaration and call`() {
        val input = """
            fun test(a) {
                a * a
            }
            test(10)
        """.trimIndent()
        val output = interpretText(input)
        assertEquals(100, output)
    }

    @Test
    fun `nested function`() {
        val input = """
            fun test(a) {
                a * a
            }
            test(test(10))
        """.trimIndent()
        val output = interpretText(input)
        assertEquals(10000, output)
    }
}
