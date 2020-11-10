package interpreter

import parser.parser.nodes.Node

class Interpreter {

    fun interpret(root: Node)= root.visit()

}
