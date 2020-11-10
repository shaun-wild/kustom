import interpreter.Interpreter
import parser.lexer.Lexer
import parser.lexer.Position
import parser.lexer.token.Token
import parser.lexer.token.TokenType
import parser.parser.Parser
import kotlin.test.assertEquals

val TEST_POSITION = Position("test", 0, 0)


fun token(name: TokenType, value: Any? = null) = Token(name, value, TEST_POSITION, TEST_POSITION)

fun parseText(text: String) = Parser(Lexer(text).lex()).parseNodes()

fun interpretText(text: String) = Interpreter().interpret(Parser(Lexer(text).lex()).parseNodes())

fun assertToString(expected: String, actual: Any?) = assertEquals(expected, actual.toString())
