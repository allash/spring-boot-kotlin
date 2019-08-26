package com.home.piperbike.api.auth

import com.home.piperbike.api.auth.dto.request.DtoAuthLoginRequest
import com.home.piperbike.api.auth.dto.request.DtoAuthRegisterRequest
import com.home.piperbike.api.auth.dto.response.DtoLoginResponse
import com.home.piperbike.api.shared.exception.user.InvalidPasswordException
import com.home.piperbike.api.shared.exception.user.UserAlreadyExistsByEmailException
import com.home.piperbike.api.shared.exception.user.UserNotFoundByEmailException
import com.home.piperbike.db.entities.DbSession
import com.home.piperbike.db.entities.DbUser
import com.home.piperbike.db.repositories.SessionRepository
import com.home.piperbike.db.repositories.UserRepository
import com.home.piperbike.helper.PasswordHelper
import com.home.piperbike.ifNotNull
import com.home.piperbike.removeHyphens
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthServiceImpl @Autowired constructor(
        private val userRepo: UserRepository,
        private val sessionRepo: SessionRepository,
        private val passwordHelper: PasswordHelper
) : AuthService {

    override fun login(body: DtoAuthLoginRequest): DtoLoginResponse {
        val user = userRepo.findOneByEmail(body.email)
                ?: throw UserNotFoundByEmailException(body.email)

        if(!passwordHelper.checkPassword(body.password, user.password))
            throw InvalidPasswordException()

        val token = UUID.randomUUID().removeHyphens()

        DbSession().also { session ->
            session.user = user
            session.userId = user.id
            session.token = token

            sessionRepo.save(session)
        }

        return DtoLoginResponse(token = token)
    }

    override fun register(body: DtoAuthRegisterRequest) {
        userRepo.findOneByEmail(body.email)
                .ifNotNull { throw UserAlreadyExistsByEmailException(body.email)  }

        DbUser().also { user ->
            user.firstName = body.firstName
            user.lastName = body.lastName
            user.email = body.email
            user.password = passwordHelper.hashPassword(password = body.password)
            userRepo.save(user)
        }
    }
}