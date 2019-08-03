package com.home.piperbike.db.hibernate

import org.hibernate.dialect.function.SQLFunction
import org.hibernate.engine.spi.Mapping
import org.hibernate.engine.spi.SessionFactoryImplementor
import org.hibernate.type.BooleanType
import org.hibernate.type.Type

class EnumArrayContainsFunction : SQLFunction {

    companion object {
        const val NAME = "enum_array_contains"
    }

    override fun render(firstArgumentType: Type?, args: MutableList<Any?>, factory: SessionFactoryImplementor?): String {
        if (args.size != 2) {
            throw IllegalArgumentException("The ${this.javaClass.name} must have 2 arguments.")
        }
        val array = args[0] as String
        val value = args[1] as String
        return "$value = ANY($array)"
    }

    override fun hasParenthesesIfNoArguments(): Boolean = true
    override fun getReturnType(firstArgumentType: Type?, mapping: Mapping?): Type = BooleanType()
    override fun hasArguments(): Boolean = true
}