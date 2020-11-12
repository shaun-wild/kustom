package parser.lexer.token

import kotlin.reflect.KClass

enum class ObjType(vararg val kotlinTypes: KClass<*>) {
    NUMBER(Int::class, Float::class),
    BOOLEAN(Boolean::class),
    STRING(String::class),
    NULL(Nothing::class),
    ANY(Any::class);

    companion object {
        fun getType(kotlinType: KClass<*>)= values().firstOrNull { it.kotlinTypes.contains(kotlinType) } ?: NULL
    }
}
