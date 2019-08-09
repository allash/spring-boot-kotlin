package com.home.piperbike.db.repositories

import com.home.piperbike.db.entities.DbUserActivity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserActivityRepository :
        JpaRepository<DbUserActivity, UUID>,
        JpaSpecificationExecutor<DbUserActivity> {
    fun findByUserId(userId: UUID): List<DbUserActivity>
    fun findOneByIdAndUserId(id: UUID, userId: UUID): DbUserActivity?
}