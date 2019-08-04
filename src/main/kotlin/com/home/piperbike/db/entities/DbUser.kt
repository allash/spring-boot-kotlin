package com.home.piperbike.db.entities

import com.home.piperbike.db.DbHelper
import com.home.piperbike.db.entities.base.BaseEntity
import com.home.piperbike.db.entities.base.BaseEntity.Companion.PUBLIC_SCHEMA_NAME
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "\"user\"", schema = PUBLIC_SCHEMA_NAME)
class DbUser : BaseEntity() {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    var id: UUID = DbHelper.EMPTY_ID
        protected set

    @Column(name = "email")
    lateinit var email: String

    @Column(name = "first_name")
    lateinit var firstName: String

    @Column(name = "last_name")
    lateinit var lastName: String

    @Column(name = "password")
    lateinit var password: String

    val fullName: String
        get() {
            return "$lastName, $firstName"
        }
}