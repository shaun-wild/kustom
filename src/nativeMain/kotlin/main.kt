import interpreter.Interpreter
import interpreter.underlineError
import parser.ParseException
import parser.lexer.Lexer
import parser.parser.Parser

fun main() {
    val interpreter = Interpreter()
    while(true) {
        print("kustom> ")
        val input = readLine()!!

        try {
            val result = parseText(input)
            println(interpreter.interpret(result))
        }catch (e: ParseException) {
            println(underlineError(input, e))
        }
    }
}

fun parseText(text: String)= Parser(Lexer(text).lex()).parseNodes()
