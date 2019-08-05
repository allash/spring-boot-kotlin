package com.home.piperbike.test.config

import com.home.dev.flyway.FixtureLoader
import com.home.dev.flyway.FlywayMigration
import com.home.piperbike.config.DatabaseConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Primary
@Profile("test")
@Configuration
class TestDatabaseMigration(
        hikariDataSource: HikariDataSource,
        fixtureLoader: FixtureLoader,
        hikariConfig: HikariConfig,
        databaseConfig: DatabaseConfig
) : FlywayMigration(
        hikariDataSource = hikariDataSource,
        fixtureLoader = fixtureLoader,
        hikariConfig = hikariConfig,
        databaseConfig = databaseConfig
) {

    private val templateDatabase = databaseConfig.name + "_tpl"

    override fun migrate() {

        if (!fixtureDatabase.endsWith("_test")) {
            throw IllegalStateException("Test database should end with '_test'")
        }

        killPostgresPool()
        clearDatabase()
        performMigration()
        restorePostgresPool()

        store()
    }

    fun store() {
        killConnections(templateDatabase)
        sql("DROP DATABASE IF EXISTS $templateDatabase")

        killPostgresPool()
        sql("CREATE DATABASE $templateDatabase TEMPLATE $fixtureDatabase")
        restorePostgresPool()
    }

    fun restore() {
        killPostgresPool()
        sql("DROP DATABASE IF EXISTS $fixtureDatabase")

        killConnections(templateDatabase)
        sql("CREATE DATABASE $fixtureDatabase TEMPLATE $templateDatabase")
        restorePostgresPool()
    }
}