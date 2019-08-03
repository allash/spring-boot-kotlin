package com.home.dev.flyway

import com.home.piperbike.config.DatabaseConfig
import com.home.piperbike.config.flyway.DbMigrationImpl
import com.home.piperbike.tryOrNull
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.pool.HikariPool
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import java.sql.Connection

@Primary
@Profile("load-fixtures")
@Configuration
class FlywayMigration (
        protected val hikariDataSource: HikariDataSource,
        protected val fixtureLoader: FixtureLoader,
        hikariConfig: HikariConfig,
        databaseConfig: DatabaseConfig
): DbMigrationImpl(hikariConfig = hikariConfig, databaseConfig = databaseConfig), AutoCloseable {
    protected var fixtureDatabase = databaseConfig.name

    private var _maintenanceDataSource: HikariDataSource? = null
    private var _maintenanceConnection: Connection? = null

    protected val maintenanceDataSource: HikariDataSource
        get() {
            var dataSource = _maintenanceDataSource
            if (dataSource == null) {
                val copy = HikariConfig().also { hikariConfig.copyStateTo(it) }
                copy.metricsTrackerFactory = null
                copy.metricRegistry = null
                copy.jdbcUrl = copy.jdbcUrl.replace(databaseConfig.name, "postgres")
                copy.poolName = "test-maintenance-connection-pool"
                copy.maximumPoolSize = MAX_MIGRATION_CONNECTIONS
                dataSource = HikariDataSource(copy)
                _maintenanceDataSource = dataSource
            }
            return dataSource
        }

    protected val maintenanceConnection: Connection
        get() {
            var connection = _maintenanceConnection
            if (connection == null) {
                connection = maintenanceDataSource.connection!!
                _maintenanceConnection = connection
            }
            return connection
        }

    override fun migrate() {
        killPostgresPool()
        clearDatabase()
        performMigration()
        restorePostgresPool()
        close()
        fixtureLoader.load()
    }

    override fun performMigration() {
        migrateSchemata()
    }

    protected fun clearDatabase() {
        killConnections(fixtureDatabase)
        sql("DROP DATABASE IF EXISTS $fixtureDatabase")
        sql("CREATE DATABASE $fixtureDatabase")
    }

    protected fun killPostgresPool() {
        hikariDataSource.hikariPoolMXBean?.let { it as HikariPool }?.also {
            it.shutdown()
        }
    }

    protected fun restorePostgresPool() {
        val newPool = HikariPool(HikariConfig().also { hikariConfig.copyStateTo(it) })
        HikariDataSource::class.java.getDeclaredField("pool").also { field ->
            field.isAccessible = true
            field.set(hikariDataSource, newPool)
        }
    }

    protected fun killConnections(database: String) {
        sql("""
                |SELECT pg_terminate_backend(pg_stat_activity.pid)
                |FROM pg_stat_activity
                |WHERE pg_stat_activity.datname = '$database'
                |AND pid <> pg_backend_pid()
            """.trimMargin(), false)
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    protected fun sql(sql: String, update: Boolean = true, connection: Connection = maintenanceConnection) {
        logger.debug(sql)
        try {
            connection.prepareStatement(sql).use { stmt ->
                if (update) stmt.executeUpdate()
                else stmt.execute()
            }
        } catch (e: Throwable) {
            logger.error("Failed to run $sql:", e)
            throw e
        }
    }

    override fun close() {
        _maintenanceConnection?.also { connection ->
            _maintenanceConnection = null
            connection.tryOrNull { it.close() }
            _maintenanceDataSource?.tryOrNull { it.evictConnection(connection) }
        }
        _maintenanceDataSource?.also { dataSource ->
            _maintenanceDataSource = null
            dataSource.tryOrNull { it.close() }
        }
    }
}