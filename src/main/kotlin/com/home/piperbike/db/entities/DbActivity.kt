package com.home.piperbike.db.entities

import com.home.piperbike.db.DbHelper
import com.home.piperbike.db.entities.base.BaseEntity
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "\"activity\"", schema = BaseEntity.PUBLIC_SCHEMA_NAME)
class DbActivity : BaseEntity() {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    var id: UUID = DbHelper.EMPTY_ID
        protected set

    @Column(name = "name")
    lateinit var name: String

    @Column(name = "description")
    var description: String? = null

    @Column(name = "distance")
    var distance: Float? = null

    @Column(name = "elapsed_time")
    var elapsedTime: Int? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    lateinit var user: DbUser

    @Column(name = "user_id", updatable = false, insertable = false)
    lateinit var userId: UUID
}