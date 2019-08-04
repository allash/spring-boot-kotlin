package com.home.piperbike.db.repositories

import com.home.piperbike.db.entities.DbActivity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ActivityRepository :
        JpaRepository<DbActivity, UUID>,
        JpaSpecificationExecutor<DbActivity> {
    fun findByUserId(userId: UUID): List<DbActivity>
}