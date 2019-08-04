package com.home.piperbike.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.home.piperbike.api.auth.AuthController
import com.home.piperbike.api.shared.dto.DtoError
import com.home.piperbike.api.shared.i18n.ErrorMessage
import com.home.piperbike.auth.AuthFilter
import com.home.piperbike.auth.TokenAuthenticationProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import javax.servlet.http.HttpServletResponse
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation

@Configuration
@EnableWebSecurity
class WebSecurityConfig @Autowired constructor(
        private val tokenAuthenticationProvider: TokenAuthenticationProvider,
        private val authFilter: AuthFilter,
        private val corsFilter: CustomCorsFilter,
        private val objectMapper: ObjectMapper
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.addFilterAfter(authFilter, UsernamePasswordAuthenticationFilter::class.java)
        http.addFilterBefore(corsFilter, AuthFilter::class.java)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.exceptionHandling()
                .authenticationEntryPoint { _, response, _ ->
                    sendErrorResponse(response, DtoError(message = ErrorMessage("access_unauthorized"), status = HttpStatus.UNAUTHORIZED))
                }
                .accessDeniedHandler { _, response, _ ->
                    sendErrorResponse(response, DtoError(message = ErrorMessage("access_forbidden"), status = HttpStatus.FORBIDDEN))
                }

        val whiteListedResources = listOf(
                AntPathRequestMatcher("/api/auth/login", HttpMethod.POST.name, true)
        )

        http.authorizeRequests()
                .requestMatchers(*whiteListedResources.toTypedArray())
                .permitAll()

        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()

        http.authorizeRequests()
                .antMatchers("/**")
                .fullyAuthenticated()

        http.csrf().disable()
    }

    private fun sendErrorResponse(response: HttpServletResponse, error: DtoError) {
        response.addHeader("Content-Type", "application/json; charset=utf-8")
        response.status = error.status
        val json = objectMapper.writeValueAsString(error)
        response.outputStream.use { it.print(json) }
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(tokenAuthenticationProvider)
    }
}
