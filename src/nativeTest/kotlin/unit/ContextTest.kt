package unit

import interpreter.Context
import parser.lexer.token.ObjType.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ContextTest {

    @Test
    fun `context set variable`() {
        val context = Context()
        context.setVariable(NUMBER, "a", true, 10)
        assertEquals(10, context.getVariable(NUMBER, "a"))
    }

    @Test
    fun `context reassign variable`() {
        val context = Context()
        context.setVariable(NUMBER, "a", true, 10)
        context.setVariable(STRING, "a", true, "Hello")
        assertEquals("Hello", context.getVariable(STRING, "a"))
        assertNull(context.getVariable(NUMBER, "a"))
    }

    @Test
    fun `cannot reassign val`() {
        val context = Context()
        context.setVariable(NUMBER, "a", false, 10)
        assertFailsWith<IllegalStateException> { context.setVariable(STRING, "a", true, "Hello") }
    }
}
