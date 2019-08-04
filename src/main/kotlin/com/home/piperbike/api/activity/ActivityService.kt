package com.home.piperbike.api.activity

import com.home.piperbike.api.activity.dto.response.DtoGetActivityResponse

interface ActivityService {
    fun getActivities(): List<DtoGetActivityResponse>
}