package com.home.piperbike.api.auth

import com.home.piperbike.api.auth.dto.request.DtoLoginRequest
import com.home.piperbike.api.auth.dto.response.DtoLoginResponse

interface AuthService {
    fun login(dto: DtoLoginRequest): DtoLoginResponse
}