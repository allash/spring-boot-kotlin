package com.home.piperbike.api.shared.exception.user

import com.home.piperbike.api.shared.exception.ServiceException
import com.home.piperbike.api.shared.i18n.ErrorMessage
import com.home.piperbike.i18n.Localization

class UserAlreadyExistsByEmailException(email: String)
    : ServiceException(ErrorMessage(Localization.Error.USER_ALREADY_EXISTS_BY_EMAIL, listOf(email)))