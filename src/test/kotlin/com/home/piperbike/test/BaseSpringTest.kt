package com.home.piperbike.test

import com.fasterxml.jackson.databind.ObjectMapper
import com.home.piperbike.Main
import com.home.piperbike.db.FixtureGenerator
import com.home.piperbike.test.config.TestDatabaseMigration
import com.zaxxer.hikari.HikariDataSource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Profile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.context.WebApplicationContext
import java.time.ZoneOffset
import java.util.*
import javax.persistence.EntityManager
import javax.sql.DataSource

@ActiveProfiles(value = ["test"])
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [Main::class])
abstract class BaseSpringTest {

    companion object {
        init {
            TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC))
        }
    }

    @LocalServerPort
    var port: Int = 0

    @Autowired
    protected lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    protected lateinit var databaseMigration: TestDatabaseMigration

    @Autowired
    @Suppress("SpringKotlinAutowiring")
    protected lateinit var fixtures: FixtureGenerator

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    @Qualifier("dataSource")
    protected lateinit var dataSource: DataSource

    @Autowired
    protected lateinit var applicationContext: ApplicationContext

    @Autowired
    protected lateinit var entityManager: EntityManager

    protected fun setUpDatabase() {
        databaseMigration.restore()

        dataSource
                .let { it as HikariDataSource }
                .also { it.hikariPoolMXBean.softEvictConnections() }
    }

    @Before
    fun setUp() {
        setUpDatabase()
        TestExtensions.OBJECT_MAPPER = objectMapper
    }

    @After
    fun tearDown() {
        Mockito.validateMockitoUsage()
    }
}
