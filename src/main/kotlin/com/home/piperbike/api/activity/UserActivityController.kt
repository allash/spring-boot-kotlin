package com.home.piperbike.api.activity

import com.home.piperbike.api.BaseController
import com.home.piperbike.api.activity.dto.response.DtoGetUserActivityResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/activities")
class UserActivityController @Autowired constructor(
        private val userActivityService: UserActivityService
): BaseController() {
    @GetMapping
    fun getUserActivities(): List<DtoGetUserActivityResponse> = userActivityService.getUserActivities()

    @GetMapping("/{id}")
    fun getUserActivityById(@PathVariable id: UUID): DtoGetUserActivityResponse = userActivityService.getUserActivityById(id)
}