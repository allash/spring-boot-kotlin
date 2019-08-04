package com.home.piperbike.api.shared.exception

import com.home.piperbike.api.shared.i18n.ErrorMessage

open class ServiceException(val i18nMessage: ErrorMessage, cause: Throwable? = null) : RuntimeException("ServiceException occurred: $i18nMessage", cause)