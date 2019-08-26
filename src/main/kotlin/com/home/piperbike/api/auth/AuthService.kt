package com.home.piperbike.api.auth

import com.home.piperbike.api.auth.dto.request.DtoAuthLoginRequest
import com.home.piperbike.api.auth.dto.request.DtoAuthRegisterRequest
import com.home.piperbike.api.auth.dto.response.DtoLoginResponse

interface AuthService {
    fun login(body: DtoAuthLoginRequest): DtoLoginResponse
    fun register(body: DtoAuthRegisterRequest)
}