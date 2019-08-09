package com.home.piperbike.api.shared.exception.user

import com.home.piperbike.api.shared.i18n.ErrorMessage
import com.home.piperbike.i18n.Localization

class UserNotFoundByEmailException(email: String)
    : UserNotFoundException(ErrorMessage(Localization.Error.USER_NOT_FOUND_BY_EMAIL, listOf(email)))