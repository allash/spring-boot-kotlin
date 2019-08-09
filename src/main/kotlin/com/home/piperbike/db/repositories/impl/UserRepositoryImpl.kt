package com.home.piperbike.db.repositories.impl

import com.home.piperbike.api.shared.exception.auth.LoginRequiredException
import com.home.piperbike.auth.TokenAuthentication
import com.home.piperbike.db.entities.DbUser
import com.home.piperbike.db.repositories.UserRepository
import com.home.piperbike.db.repositories.custom.UserRepositoryCustom
import com.home.piperbike.findOne
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder

class UserRepositoryImpl : UserRepositoryCustom {

    @Autowired
    lateinit var userRepo: UserRepository

    override fun expectCurrentUser(): DbUser {
        return currentUser() ?: throw LoginRequiredException()
    }

    override fun currentUser(): DbUser? {
        val sessionUser = SecurityContextHolder.getContext().authentication
                ?.let { it as? TokenAuthentication }?.principal?.user ?: return null
        return userRepo.findOne(sessionUser.id)
    }
}