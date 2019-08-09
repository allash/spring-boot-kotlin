package com.home.piperbike.api.activity

import com.home.piperbike.api.activity.dto.response.DtoGetUserActivityResponse
import java.util.*

interface UserActivityService {
    fun getUserActivities(): List<DtoGetUserActivityResponse>
    fun getUserActivityById(id: UUID): DtoGetUserActivityResponse
}