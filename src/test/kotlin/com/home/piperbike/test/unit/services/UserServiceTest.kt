package com.home.piperbike.test.unit.services

import com.home.piperbike.api.user.UserMapper
import com.home.piperbike.api.user.UserService
import com.home.piperbike.api.user.UserServiceImpl
import com.home.piperbike.db.entities.DbUser
import com.home.piperbike.db.repositories.UserRepository
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class UserServiceTest {
    private lateinit var userService: UserService

    @Mock
    private lateinit var userRepo: UserRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        userService = UserServiceImpl(userRepo, UserMapper())
    }

    @Test
    fun canGetUsers() {
        val dbUsers = listOf(
                DbUser().also {
                    it.email = "${UUID.randomUUID()}@mail.com"
                    it.firstName = UUID.randomUUID().toString()
                    it.lastName = UUID.randomUUID().toString()
                },
                DbUser().also {
                    it.email = "${UUID.randomUUID()}@mail.com"
                    it.firstName = UUID.randomUUID().toString()
                    it.lastName = UUID.randomUUID().toString()
                }

        )

        Mockito.`when`(userRepo.findAll()).thenReturn(dbUsers)

        val dtoUsers = userService.getUsers()

        Assertions.assertThat(dbUsers.size).isEqualTo(dtoUsers.size)
    }
}