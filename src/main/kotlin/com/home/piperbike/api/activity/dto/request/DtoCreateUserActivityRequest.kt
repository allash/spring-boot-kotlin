package com.home.piperbike.api.activity.dto.request

import com.home.piperbike.validation.NotBlank

data class DtoCreateUserActivityRequest (
        @field:NotBlank
        val name: String,
        val description: String?,
        val distance: Float?,
        val elapsedTime: Int?
)