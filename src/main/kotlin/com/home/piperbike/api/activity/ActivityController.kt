package com.home.piperbike.api.activity

import com.home.piperbike.api.BaseController
import com.home.piperbike.api.activity.dto.response.DtoGetActivityResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/activities")
class ActivityController @Autowired constructor(
        private val activityService: ActivityService
): BaseController() {
    @GetMapping
    fun getActivities(): List<DtoGetActivityResponse> = activityService.getActivities()
}