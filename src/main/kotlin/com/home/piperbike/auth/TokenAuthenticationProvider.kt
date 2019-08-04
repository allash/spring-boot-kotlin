package com.home.piperbike.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class TokenAuthenticationProvider @Autowired constructor() : AuthenticationProvider {

    companion object {
        private val SUPPORTED_TOKENS = setOf(TokenAuthentication::class.java)
    }

    override fun authenticate(authentication: Authentication): Authentication {
        if (authentication !is TokenAuthentication)
            throw IllegalStateException("Unsupported authentication object: ${authentication::class}")
        return authentication
    }

    override fun supports(authentication: Class<*>): Boolean = SUPPORTED_TOKENS.contains(authentication)
}