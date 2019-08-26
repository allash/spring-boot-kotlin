package com.home.piperbike.auth

import com.home.piperbike.auth.Rights.CAN_CREATE_CLUB_POSTS
import com.home.piperbike.auth.Rights.CAN_CREATE_USER_ACTIVITY
import com.home.piperbike.auth.Rights.CAN_DELETE_USER_ACTIVITY
import com.home.piperbike.auth.Rights.CAN_EDIT_USER_ACTIVITY
import com.home.piperbike.auth.Rights.CAN_READ_CLUB_USERS
import com.home.piperbike.auth.Rights.CAN_READ_USER_ACTIVITIES
import com.home.piperbike.auth.Rights.CAN_READ_USER_PROFILE

enum class Role(val rights: Set<String>) {
    CLUB_ADMIN(setOf(
            CAN_CREATE_CLUB_POSTS,
            CAN_READ_CLUB_USERS
    )),
    USER(setOf(
            CAN_READ_USER_ACTIVITIES,
            CAN_EDIT_USER_ACTIVITY,
            CAN_DELETE_USER_ACTIVITY,
            CAN_CREATE_USER_ACTIVITY,
            CAN_READ_USER_PROFILE
            ))
}