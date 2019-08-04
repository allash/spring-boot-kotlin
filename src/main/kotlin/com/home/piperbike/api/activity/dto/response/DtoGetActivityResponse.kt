package com.home.piperbike.api.activity.dto.response

import java.time.Instant
import java.util.*

data class DtoGetActivityResponse (
        val id: UUID,
        val name: String,
        val description: String?,
        val distance: Float?,
        val elapsedTime: Int?,
        val createdAt: Instant
)