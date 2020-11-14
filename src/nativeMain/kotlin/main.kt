import files.File
import interpreter.Interpreter
import interpreter.underlineError
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import parser.ParseException
import parser.Parser
import parser.lexer.Lexer
import platform.posix.*

fun main(args: Array<String>) {
    val interpreter = Interpreter()

    if(args.size == 1) {
        val file = File(args[0])

        if(file.exists()) {
            val contents = file.readAllLines()
            runCode(file.name, contents, interpreter)
            return
        } else {
            error("File not found ${file.filename}")
        }
    }

    while (true) {
        print("kustom> ")
        val input = readLine()!!
        runCode("stdin", input, interpreter)
    }
}

fun runCode(filename: String = "stdin", input: String, interpreter: Interpreter) {
    try {
        val result = parseText(filename, input)
        println(interpreter.interpret(result))
    } catch (e: ParseException) {
        println(underlineError(input, e))
    }
}

fun parseText(filename: String, text: String) = Parser(Lexer(text, filename).lex()).parseNodes()
