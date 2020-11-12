package interpreter

import parser.lexer.token.ObjType

class Context {

    var parent: Context? = null
    val variables = mutableMapOf<TypeKey, Any?>()

    fun getVariable(type: ObjType, name: String, reassignable: Boolean = true): Any? {
        val key = TypeKey(type, name, reassignable)
//        println("Trying to get $key in $variables")
        return variables.entries.find { it.key == key }?.value
    }

    fun setVariable(type: ObjType, name: String, reassignable: Boolean = true, value: Any?): Any? {
        val key = TypeKey(type, name, reassignable)
        val existing = variables.keys.find { it.name == name }

        if (existing != null) {
            if (existing.reassignable) {
                variables.remove(existing)
                variables[key] = value
            } else {
                throw IllegalStateException("Cannot reassign 'val $name'")
            }
        } else {
            variables[key] = value
        }

        return value
    }

    fun pushContext(parent: Context): Context {
        this.parent = parent
        return this
    }

    fun popContext(): Context? {
        val parent = this.parent
        this.parent = null
        return parent
    }

    private fun variableExists(name: String) = variables.entries.find { it.key.name == name }
}

data class TypeKey(val type: ObjType, val name: String, val reassignable: Boolean) {
    override fun equals(other: Any?): Boolean {
        if (other is TypeKey) {
            val typeMatch = (this.type == ObjType.ANY || other.type == ObjType.ANY || this.type == other.type)
            return typeMatch && this.name == other.name
        }

        return false
    }
}
