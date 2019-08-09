package com.home.piperbike.auth

import com.home.piperbike.api.shared.dto.DtoSession
import com.home.piperbike.api.shared.exception.auth.LoginRequiredException
import com.home.piperbike.config.AppConfig
import com.home.piperbike.db.entities.DbUser
import com.home.piperbike.db.repositories.SessionRepository
import com.home.piperbike.db.repositories.UserRepository
import com.home.piperbike.findOne
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
        private val sessionRepo: SessionRepository,
        private val userRepo: UserRepository
) : OncePerRequestFilter() {

    @Transactional(rollbackFor = [Throwable::class])
    override fun doFilterInternal(
            httpRequest: HttpServletRequest,
            httpResponse: HttpServletResponse,
            chain: FilterChain) {

        val token = parseToken(httpRequest.getHeader(AppConfig.AUTH_TOKEN_HEADER))

        token?.let {
            sessionRepo.findOneByToken(it)?.let { session ->
                userRepo.findOne(session.userId)?.let { user ->
                    val authentication = createTokenAuthentication(session.token, user)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }

        }

        chain.doFilter(httpRequest, httpResponse)
    }

    private fun createTokenAuthentication(token: String, user: DbUser): TokenAuthentication = TokenAuthentication(token)
            .let { authentication ->
                authentication.principal = DtoSession(
                        token = token,
                        user = DtoSession.DtoSessionUser(
                                id = user.id,
                                rights = user.role.rights.toList()
                        ))
                authentication.isAuthenticated = true
                authentication
            }

    private fun parseToken(token: String?): String? {
        return token?.takeIf { it.isNotBlank() && it != "null" && it != "undefined" }
    }
}