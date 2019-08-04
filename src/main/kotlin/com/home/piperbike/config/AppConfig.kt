package com.home.piperbike.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("app")
class AppConfig {
    companion object {
        const val AUTH_TOKEN_HEADER = "x-auth-token"
    }
}