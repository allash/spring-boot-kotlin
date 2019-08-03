package com.home.piperbike

import com.home.dev.flyway.FlywayMigration
import com.home.piperbike.config.flyway.DbMigration
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener

@SpringBootApplication(
        exclude = [
            FlywayAutoConfiguration::class,
            XADataSourceAutoConfiguration::class,
            JacksonAutoConfiguration::class,
            SpringDataWebAutoConfiguration::class
        ],
        scanBasePackageClasses = [
            Main::class,
            FlywayMigration::class
        ]
)
class Main @Autowired constructor(
        protected val dbMigration: DbMigration
) {
    companion object {
        private val logger = LoggerFactory.getLogger(Main::class.java)
    }

    @EventListener
    fun afterContextRefreshed(event: ContextRefreshedEvent) {
        dbMigration.migrate()
        logger.info("System started.")
    }
}

fun main(args: Array<String>) {
    try {
        @Suppress("SpreadOperator")
        SpringApplication.run(Main::class.java, *args)
    } catch (e: Throwable) {
        LoggerFactory.getLogger(Main::class.java).error("Application startup failed: ", e)
        System.exit(1)
    }
}
