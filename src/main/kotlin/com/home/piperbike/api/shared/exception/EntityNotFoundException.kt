package com.home.piperbike.api.shared.exception

import com.home.piperbike.api.shared.i18n.ErrorMessage

abstract class EntityNotFoundException(msg: ErrorMessage) : ServiceException(msg)