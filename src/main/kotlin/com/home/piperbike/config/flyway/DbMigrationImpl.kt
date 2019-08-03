package com.home.piperbike.config.flyway

import com.home.piperbike.config.DatabaseConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class DbMigrationImpl @Autowired constructor(
        protected val hikariConfig: HikariConfig,
        protected val databaseConfig: DatabaseConfig
) : DbMigration {

    companion object {
        const val MAX_MIGRATION_CONNECTIONS = 1
        const val MIGRATION_PATH = "classpath:/db/migration/public"
    }

    protected val logger = LoggerFactory.getLogger(DbMigrationImpl::class.java)

    override fun migrate() {
        if (!databaseConfig.loadMigrations) {
            logger.info("Flyway migrations are disabled, skipping")
            return
        }

        performMigration()
    }

    protected fun performMigration() {
        migrateSchemata()
    }

    protected fun migrateSchemata() {
        val flywayDataSource = getDataSource()
        val connection = flywayDataSource.connection

        connection.prepareStatement("BEGIN;").executeUpdate()
        connection.prepareStatement("CREATE SCHEMA IF NOT EXISTS \"public\";").executeUpdate()

        getFlyway(flywayDataSource).migrate()

        connection.prepareStatement("COMMIT;").executeUpdate()
        flywayDataSource.closeOriginalDataSource()
    }

    protected fun getFlyway(dataSource: FlywayMigrationDataSource): Flyway {
        val conf = Flyway.configure()
                .dataSource(dataSource)
                .schemas("public")
                .locations(MIGRATION_PATH)
        return Flyway(conf)
    }

    protected fun getDataSource() : FlywayMigrationDataSource {
        val config = HikariConfig().also { hikariConfig.copyStateTo(it) }
        config.metricsTrackerFactory = null
        config.metricRegistry = null
        config.maximumPoolSize = MAX_MIGRATION_CONNECTIONS
        config.poolName = "flyway-connection-pool"
        val dataSource = HikariDataSource(config)
        val connection = dataSource.connection!!
        return FlywayMigrationDataSource(originalDataSource = dataSource, originalConnection = connection)
    }
}