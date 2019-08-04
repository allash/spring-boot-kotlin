package com.home.piperbike.api.shared.exception.user

import com.home.piperbike.api.shared.i18n.ErrorMessage

class UserNotFoundByEmailException(email: String) : UserNotFoundException(ErrorMessage("user_not_found_by_email", listOf(email)))