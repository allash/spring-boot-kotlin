package com.home.piperbike.test.integration.auth

import com.home.piperbike.api.auth.AuthController
import com.home.piperbike.api.auth.dto.request.DtoLoginRequest
import com.home.piperbike.api.auth.dto.response.DtoLoginResponse
import com.home.piperbike.api.shared.exception.user.InvalidPasswordException
import com.home.piperbike.api.shared.exception.user.UserNotFoundByEmailException
import com.home.piperbike.db.entities.DbUser
import com.home.piperbike.db.repositories.SessionRepository
import com.home.piperbike.test.andExpectError
import com.home.piperbike.test.andReturnResult
import com.home.piperbike.test.integration.BaseControllerTest
import com.home.piperbike.test.post
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

class AuthLoginTest : BaseControllerTest(AuthController::login) {

    @Autowired
    private lateinit var sessionRepository: SessionRepository

    private fun buildRequest(body: DtoLoginRequest): MockHttpServletRequestBuilder =
            testTarget.post(body = body)

    class AuthContext(val user: DbUser, val body: DtoLoginRequest)

    private val validContext = {
        val password = "asdf"
        val user = fixtures.createUser(password = password)
        val body = DtoLoginRequest(
                email = user.email,
                password = password
        )

        AuthContext(user = user, body = body)
    }

    @Test
    fun canLoginWithValidCredentials() {
        val ctx = validContext()

        val sessionsBefore = sessionRepository.findAll()
        assertThat(sessionsBefore.size).isEqualTo(0)

        val result = mvc.perform(buildRequest(ctx.body))
                .andExpect(status().isOk)
                .andReturnResult<DtoLoginResponse>()

        assertThat(result.token).isNotNull()

        val sessionsAfter = sessionRepository.findAll()
        assertThat(sessionsAfter.size).isEqualTo(1)

        val createdSession = sessionsAfter[0]
        assertThat(createdSession.userId).isEqualTo(ctx.user.id)
    }

    @Test
    fun cannotLoginWithInvalidEmail() {
        val ctx = validContext()
        val body = ctx.body.copy(email = "demo1234@mail.com")

        mvc.perform(buildRequest(body))
                .andExpect(status().isNotFound)
                .andExpectError(UserNotFoundByEmailException(body.email))
    }

    @Test
    fun cannotLoginWithInvalidEmailFormat() {
        val ctx = validContext()
        val body = ctx.body.copy(email = "demo1234mail")

        mvc.perform(buildRequest(body))
                .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun cannotLoginWithInvalidPassword() {
        val ctx = validContext()
        val body = ctx.body.copy(password = UUID.randomUUID().toString())

        mvc.perform(buildRequest(body))
                .andExpect(status().isNotFound)
                .andExpectError(InvalidPasswordException())
    }
}