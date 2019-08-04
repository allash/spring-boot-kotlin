package com.home.piperbike.db.repositories

import com.home.piperbike.db.entities.DbSession
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SessionRepository :
        JpaRepository<DbSession, UUID>,
        JpaSpecificationExecutor<DbSession>