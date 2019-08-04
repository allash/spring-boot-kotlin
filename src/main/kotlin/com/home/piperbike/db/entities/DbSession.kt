package com.home.piperbike.db.entities

import com.home.piperbike.db.DbHelper
import com.home.piperbike.db.entities.base.BaseEntity
import com.home.piperbike.db.entities.base.BaseEntity.Companion.PUBLIC_SCHEMA_NAME
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "\"session\"", schema = PUBLIC_SCHEMA_NAME)
class DbSession : BaseEntity() {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    var id: UUID = DbHelper.EMPTY_ID
        protected set

    @Column(name = "token")
    lateinit var token: String

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    lateinit var user: DbUser

    @Column(name = "user_id", insertable = false, updatable = false)
    lateinit var userId: UUID
}