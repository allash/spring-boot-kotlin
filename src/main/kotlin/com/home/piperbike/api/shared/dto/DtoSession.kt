package com.home.piperbike.api.shared.dto

import java.util.*

data class DtoSession(
        val token: String,
        val user: DtoSessionUser
) {
    data class DtoSessionUser(val id: UUID, val rights: List<String>)
}