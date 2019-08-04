package com.home.piperbike.auth

import com.home.piperbike.api.shared.dto.DtoSession
import com.home.piperbike.config.AppConfig
import com.home.piperbike.db.repositories.SessionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthFilter @Autowired constructor(
        private val sessionRepo: SessionRepository
) : OncePerRequestFilter() {

    @Transactional(rollbackFor = [Throwable::class])
    override fun doFilterInternal(httpRequest: HttpServletRequest, httpResponse: HttpServletResponse, chain: FilterChain) {
        val token = parseToken(httpRequest.getHeader(AppConfig.AUTH_TOKEN_HEADER))

        token?.let {
            sessionRepo.findOneByToken(it)?.let { session ->
                TokenAuthentication(token).also { authentication ->
                    authentication.principal = DtoSession(
                            token = session.token,
                            user = DtoSession.DtoSessionUser(
                                    id = session.user.id,
                                    rights = listOf()
                            ))
                    authentication.isAuthenticated = true

                    SecurityContextHolder.getContext().authentication = authentication
                }
            }

        }

        chain.doFilter(httpRequest, httpResponse)
    }

    private fun parseToken(token: String?): String? {
        return token?.takeIf { it.isNotBlank() && it != "null" && it != "undefined" }
    }
}