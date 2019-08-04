package com.home.piperbike.api.shared.exception.user

import com.home.piperbike.api.shared.i18n.ErrorMessage

class InvalidPasswordException : UserNotFoundException(ErrorMessage("invalid_password"))