package com.home.piperbike.api.activity.dto.response

import java.time.Instant
import java.util.*

data class DtoGetUserActivityResponse (
        val id: UUID,
        val name: String,
        val description: String? = null,
        val distance: Float? = null,
        val elapsedTime: Int? = null,
        val createdAt: Instant
)