package interpreter

import nodes.nodes.Node

class Interpreter {

    val context = Context()

    fun interpret(root: Node)= root.visit(context)

}
