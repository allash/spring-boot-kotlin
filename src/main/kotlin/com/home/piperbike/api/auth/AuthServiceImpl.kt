package com.home.piperbike.api.auth

import com.home.piperbike.api.auth.dto.request.DtoLoginRequest
import com.home.piperbike.api.auth.dto.response.DtoLoginResponse
import com.home.piperbike.api.shared.exception.user.UserNotFoundByEmailException
import com.home.piperbike.db.entities.DbSession
import com.home.piperbike.db.repositories.SessionRepository
import com.home.piperbike.db.repositories.UserRepository
import com.home.piperbike.helper.PasswordHelper
import com.home.piperbike.removeHyphens
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.util.*

@Service
class AuthServiceImpl @Autowired constructor(
        private val userRepo: UserRepository,
        private val sessionRepo: SessionRepository,
        private val passwordHelper: PasswordHelper
) : AuthService {

    override fun login(dto: DtoLoginRequest): DtoLoginResponse {
        val user = userRepo.findOneByEmail(dto.email)
                ?: throw UserNotFoundByEmailException(dto.email)

        if(!passwordHelper.checkPassword(dto.password, user.password))
            throw RuntimeException("password")

        val token = UUID.randomUUID().removeHyphens()

        DbSession().also { session ->
            session.user = user
            session.userId = user.id
            session.token = token

            sessionRepo.save(session)
        }

        return DtoLoginResponse(token = token)
    }
}