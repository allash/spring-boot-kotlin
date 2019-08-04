package com.home.piperbike.config.flyway

import com.home.piperbike.tryOrNull
import com.zaxxer.hikari.HikariDataSource
import java.io.PrintWriter
import java.sql.Connection
import java.sql.SQLException
import java.sql.SQLFeatureNotSupportedException
import java.util.logging.Logger
import javax.sql.DataSource

private class FlywayConnection(val originalConnection: Connection): Connection by originalConnection {
    override fun close() { }
}

class FlywayMigrationDataSource(
        val originalDataSource: HikariDataSource? = null,
        val originalConnection: Connection
): DataSource {

    private val cachedConnection = FlywayConnection(originalConnection)

    override fun getConnection(): Connection = cachedConnection
    override fun getConnection(username: String?, password: String?): Connection = cachedConnection

    override fun setLogWriter(out: PrintWriter?) = Unit

    override fun setLoginTimeout(seconds: Int) = Unit

    override fun isWrapperFor(iface: Class<*>?): Boolean = false

    override fun <T : Any?> unwrap(iface: Class<T>?): T = throw SQLException("cannot unwrap $iface")

    override fun getParentLogger(): Logger = throw SQLFeatureNotSupportedException("getParentLogger not supported")

    override fun getLogWriter(): PrintWriter? = null
    override fun getLoginTimeout(): Int = 0

    fun closeOriginalDataSource() {
        originalConnection.tryOrNull { it.close() }
        originalDataSource?.also { dataSource ->
            dataSource.tryOrNull { it.evictConnection(originalConnection) }
            dataSource.tryOrNull { it.close() }
        }
    }
}