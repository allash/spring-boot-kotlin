package com.home.piperbike.api.activity

import com.home.piperbike.api.activity.dto.response.DtoGetUserActivityResponse
import com.home.piperbike.db.entities.DbUserActivity
import org.springframework.stereotype.Component

@Component
class UserActivityMapper {
    fun toGetActivityResponse(userActivity: DbUserActivity): DtoGetUserActivityResponse = DtoGetUserActivityResponse(
            id = userActivity.id,
            name = userActivity.name,
            description = userActivity.description,
            distance = userActivity.distance,
            elapsedTime = userActivity.elapsedTime,
            createdAt = userActivity.createdAt
    )

    fun toGetActivitiesResponse(userActivities: List<DbUserActivity>): List<DtoGetUserActivityResponse> =
            userActivities.map { toGetActivityResponse(it) }
}