package com.home.piperbike

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
        exclude = [
            FlywayAutoConfiguration::class,
            DataSourceAutoConfiguration::class,
            JacksonAutoConfiguration::class
        ],
        scanBasePackageClasses = [
            Main::class
        ]
)
class Main

fun main(args: Array<String>) {
    runApplication<Main>(*args)
}
