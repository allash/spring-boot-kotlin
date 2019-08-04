package com.home.piperbike.api.user.dto.response

import java.util.*

data class DtoGetUserResponse(
        val id: UUID,
        val email: String,
        val firstName: String,
        val lastName: String
)