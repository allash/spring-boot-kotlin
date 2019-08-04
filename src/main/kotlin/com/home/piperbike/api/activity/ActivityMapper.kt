package com.home.piperbike.api.activity

import com.home.piperbike.api.activity.dto.response.DtoGetActivityResponse
import com.home.piperbike.db.entities.DbActivity
import org.springframework.stereotype.Component

@Component
class ActivityMapper {
    fun toGetActivityResponse(activity: DbActivity): DtoGetActivityResponse = DtoGetActivityResponse(
            id = activity.id,
            name = activity.name,
            description = activity.description,
            distance = activity.distance,
            elapsedTime = activity.elapsedTime,
            createdAt = activity.createdAt
    )

    fun toGetActivitiesResponse(activities: List<DbActivity>): List<DtoGetActivityResponse> =
            activities.map { toGetActivityResponse(it) }
}