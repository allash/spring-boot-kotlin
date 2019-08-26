package com.home.piperbike.api.user

import com.home.piperbike.api.user.dto.response.DtoGetUserResponse
import com.home.piperbike.auth.Rights
import org.springframework.security.access.prepost.PreAuthorize

interface UserService {
    fun getUsers(): List<DtoGetUserResponse>
    fun createUser()

    @PreAuthorize("hasAuthority('${Rights.CAN_READ_USER_PROFILE}')")
    fun getUser(): DtoGetUserResponse
}