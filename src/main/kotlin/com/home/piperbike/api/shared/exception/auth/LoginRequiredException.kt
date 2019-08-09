package com.home.piperbike.api.shared.exception.auth

import com.home.piperbike.api.shared.exception.ServiceException
import com.home.piperbike.api.shared.i18n.ErrorMessage
import com.home.piperbike.i18n.Localization

class LoginRequiredException : ServiceException(ErrorMessage(Localization.Error.LOGIN_REQUIRED))