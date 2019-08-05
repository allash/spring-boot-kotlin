package com.home.piperbike.test.integration.user

import com.home.piperbike.api.user.UserController
import com.home.piperbike.api.user.dto.response.DtoGetUserResponse
import com.home.piperbike.db.entities.DbSession
import com.home.piperbike.db.entities.DbUser
import com.home.piperbike.test.andReturnResultList
import com.home.piperbike.test.get
import com.home.piperbike.test.integration.BaseControllerTest
import com.home.piperbike.test.withSessionToken
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class GetUsersTest : BaseControllerTest(UserController::getUsers) {

    private class AuthContext(val users: List<DbUser>, val session: DbSession)

    private val validContext = {

        val users = listOf(
                fixtures.createUser(),
                fixtures.createUser(),
                fixtures.createUser()
        )

        val session = fixtures.createSession(users[0])

        AuthContext(users = users, session = session)
    }

    @Test
    fun canGetUsers() {
        val ctx = validContext()

        val result = mvc.perform(testTarget.get()
                .withSessionToken(ctx.session.token))
                .andReturnResultList<DtoGetUserResponse>()

        assertThat(result.size).isEqualTo(ctx.users.size)
    }

    @Test
    fun notAuthenticated() {
        mvc.perform(testTarget.get())
                .andExpect(status().isUnauthorized)
    }
}