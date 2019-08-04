package com.home.piperbike.api.shared.exception.user

import com.home.piperbike.api.shared.exception.EntityNotFoundException
import com.home.piperbike.api.shared.i18n.ErrorMessage

abstract class UserNotFoundException(msg: ErrorMessage) : EntityNotFoundException(msg)