package com.home.piperbike.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
@ConfigurationProperties("app.database")
class DatabaseConfig {

    companion object {
        private val logger = LoggerFactory.getLogger(DatabaseConfig::class.java)
    }

    var host = ""
    var port = 0
    var name = ""
    var user = ""
    var password = ""
    var loadMigrations = false

    @Value("\${spring.datasource.url}")
    lateinit var url: String

    @Value("\${spring.datasource.driver-class-name}")
    lateinit var driverClassName: String

    @PostConstruct
    fun postConstruct() {
        logger.debug("host: $host")
        logger.debug("port: $port")
        logger.debug("url: $url")
        logger.debug("driverClassName: $driverClassName")
        if (host.isBlank()) throw IllegalStateException("database host must not be blank")
        if (port < 1) throw IllegalStateException("database port must be above zero")
        if (name.isBlank()) throw IllegalStateException("database name must not be blank")
        if (user.isBlank()) throw IllegalStateException("database user must not be blank")
        if (password.isBlank()) throw IllegalStateException("database password must not be blank")
    }
}