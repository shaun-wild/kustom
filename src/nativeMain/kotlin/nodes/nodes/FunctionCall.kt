package nodes.nodes

import interpreter.Context
import parser.ParseException
import parser.lexer.token.ObjType
import parser.lexer.token.Token

class FunctionCall(start: Token, val function: Node, val arguments: List<Node>) : Node(start) {

    override fun visit(context: Context): Any? {
        val function = function.visit(context)

        if(function !is FunctionNode) {
            throw ParseException("Expected function", this.function.start, this.function.end)
        }

        val funContext = context.pushContext(Context())

        function.arguments.forEachIndexed { index, arg ->
            val argument = arguments[index].visit(context)
            funContext.setVariable(ObjType.ANY, arg, false, argument)
        }

        val result = function.body.visit(funContext)
        context.popContext()
        return result
    }

    override fun toString()= "call <${function}>(${arguments.joinToString()})"
}
