package com.home.piperbike.db.repositories.custom

import com.home.piperbike.db.entities.DbUser

interface UserRepositoryCustom {
    fun expectCurrentUser(): DbUser
    fun currentUser(): DbUser?
}