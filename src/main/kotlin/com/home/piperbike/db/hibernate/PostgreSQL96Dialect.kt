package com.home.piperbike.db.hibernate

import org.hibernate.dialect.PostgreSQL93Dialect
import org.hibernate.type.StringType
import java.sql.Types

@Suppress("unused")
class PostgreSQL96Dialect : PostgreSQL93Dialect() {
    init {
        @Suppress("MagicNumber")
        registerHibernateType(1111, StringType.INSTANCE.name)
        registerColumnType(Types.JAVA_OBJECT, "jsonb")
        registerFunction(EnumArrayContainsFunction.NAME, EnumArrayContainsFunction())
    }
}