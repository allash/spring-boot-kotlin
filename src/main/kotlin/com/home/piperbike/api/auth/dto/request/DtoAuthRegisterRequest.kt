package com.home.piperbike.api.auth.dto.request

import com.home.piperbike.validation.Email
import com.home.piperbike.validation.NotBlank

data class DtoAuthRegisterRequest (
        @field:NotBlank
        val firstName: String,

        @field:NotBlank
        val lastName: String,

        @field:Email
        val email: String,

        @field:NotBlank
        val password: String
)