package com.home.piperbike.api.user

import com.home.piperbike.api.user.dto.response.DtoGetUserResponse

interface UserService {
    fun getUsers(): List<DtoGetUserResponse>
    fun createUser()
}