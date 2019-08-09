package com.home.piperbike.api.activity

import com.home.piperbike.api.activity.dto.response.DtoGetUserActivityResponse
import com.home.piperbike.api.shared.exception.activity.UserActivityNotFoundByIdException
import com.home.piperbike.db.repositories.UserActivityRepository
import com.home.piperbike.db.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserActivityServiceImpl @Autowired constructor(
        private val userRepo: UserRepository,
        private val userActivityRepo: UserActivityRepository,
        private val mapperUser: UserActivityMapper
) : UserActivityService {
    override fun getUserActivities(): List<DtoGetUserActivityResponse> {
        val user = userRepo.expectCurrentUser()
        val activities = userActivityRepo.findByUserId(user.id)
        return mapperUser.toGetActivitiesResponse(activities)
    }

    override fun getUserActivityById(id: UUID): DtoGetUserActivityResponse {
        val user = userRepo.expectCurrentUser()
        return userActivityRepo.findOneByIdAndUserId(id, user.id)
                .let { it ?: throw UserActivityNotFoundByIdException(id) }
                .let { mapperUser.toGetActivityResponse(it) }
    }
}