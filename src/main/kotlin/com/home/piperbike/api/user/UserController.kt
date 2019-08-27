package com.home.piperbike.api.user

import com.home.piperbike.api.BaseController
import com.home.piperbike.api.user.dto.response.DtoGetUserResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController @Autowired constructor(
        private val userService: UserService
) : BaseController() {

    @GetMapping
    fun getUsers(): List<DtoGetUserResponse> = userService.getUsers()

    @GetMapping("/profile")
    fun getUser(): DtoGetUserResponse = userService.getUser()
}