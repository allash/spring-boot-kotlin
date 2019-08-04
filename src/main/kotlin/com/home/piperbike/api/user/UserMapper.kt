package com.home.piperbike.api.user

import com.home.piperbike.api.user.dto.response.DtoGetUserResponse
import com.home.piperbike.db.entities.DbUser
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun toDtoGetUserResponse(user: DbUser): DtoGetUserResponse = DtoGetUserResponse(
            id = user.id,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName
    )

    fun toDtoGetUsersResponse(users: List<DbUser>): List<DtoGetUserResponse> = users.map { toDtoGetUserResponse(it) }
}