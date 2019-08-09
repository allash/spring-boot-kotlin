package com.home.piperbike.api.shared.exception.activity

import com.home.piperbike.api.shared.exception.EntityNotFoundException
import com.home.piperbike.api.shared.i18n.ErrorMessage

abstract class UserActivityNotFoundException(msg: ErrorMessage): EntityNotFoundException(msg)