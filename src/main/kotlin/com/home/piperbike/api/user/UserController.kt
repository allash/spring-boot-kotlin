package com.home.piperbike.api.user

import com.home.piperbike.api.BaseController
import com.home.piperbike.api.user.dto.response.DtoGetUsersResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController : BaseController() {

    @GetMapping
    fun getUsers(): List<DtoGetUsersResponse> = listOf()
}