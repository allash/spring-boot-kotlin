package com.home.piperbike.db.entities.base

import com.home.piperbike.db.entities.DbModel
import java.io.Serializable
import java.time.Instant
import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.persistence.PreUpdate

@MappedSuperclass
abstract class BaseEntity : DbModel, Serializable {

    companion object {
        const val PUBLIC_SCHEMA_NAME = "\"public\""
    }

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME(6)")
    var createdAt: Instant = Instant.now()

    @Column(name = "updated_at", nullable = true, columnDefinition = "DATETIME(6)")
    var updatedAt: Instant? = null

    @PreUpdate
    private fun beforeUpdate() {
        updatedAt = Instant.now()
    }
}