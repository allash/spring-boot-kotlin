package com.home.piperbike.api.activity

import com.home.piperbike.api.BaseController
import com.home.piperbike.api.activity.dto.request.DtoCreateUserActivityRequest
import com.home.piperbike.api.activity.dto.response.DtoGetUserActivityResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/activities")
class UserActivityController @Autowired constructor(
        private val userActivityService: UserActivityService
): BaseController() {
    @GetMapping
    fun getUserActivities(): List<DtoGetUserActivityResponse> = userActivityService.getUserActivities()

    @GetMapping("/{id}")
    fun getUserActivityById(@PathVariable id: UUID): DtoGetUserActivityResponse =
            userActivityService.getUserActivityById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUserActivity(@Valid @RequestBody body: DtoCreateUserActivityRequest) =
            userActivityService.createUserActivity(body)
}