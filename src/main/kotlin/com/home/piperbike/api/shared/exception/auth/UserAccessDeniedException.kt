package com.home.piperbike.api.shared.exception.auth

import com.home.piperbike.api.shared.exception.ServiceException
import com.home.piperbike.api.shared.i18n.ErrorMessage

abstract class UserAccessDeniedException(msg: ErrorMessage) : ServiceException(msg)