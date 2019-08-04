package com.home.piperbike.api.activity

import com.home.piperbike.api.activity.dto.response.DtoGetActivityResponse
import com.home.piperbike.db.repositories.ActivityRepository
import com.home.piperbike.db.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ActivityServiceImpl @Autowired constructor(
        private val userRepository: UserRepository,
        private val activityRepo: ActivityRepository,
        private val mapper: ActivityMapper
) : ActivityService {
    override fun getActivities(): List<DtoGetActivityResponse> {
        val user = userRepository.findOneByEmail("john.doe@mail.com")!! // TODO: switch to current user from session
        val activities = activityRepo.findByUserId(user.id)
        return mapper.toGetActivitiesResponse(activities)
    }
}