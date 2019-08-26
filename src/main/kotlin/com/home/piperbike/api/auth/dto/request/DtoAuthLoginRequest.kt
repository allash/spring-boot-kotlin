package com.home.piperbike.api.auth.dto.request

import com.home.piperbike.validation.Email
import javax.validation.constraints.NotBlank

data class DtoAuthLoginRequest(
        @field:Email
        val email: String,

        @field:NotBlank
        val password: String
)