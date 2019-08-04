package com.home.piperbike.api.user

import com.home.piperbike.api.user.dto.response.DtoGetUserResponse
import com.home.piperbike.db.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl @Autowired constructor(
        private val userRepo: UserRepository,
        private val mapper: UserMapper
) : UserService {

    override fun getUsers(): List<DtoGetUserResponse> {
        return mapper.toDtoGetUsersResponse(userRepo.findAll())
    }

    override fun createUser() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}