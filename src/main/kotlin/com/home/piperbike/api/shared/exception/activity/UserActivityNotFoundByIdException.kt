package com.home.piperbike.api.shared.exception.activity

import com.home.piperbike.api.shared.i18n.ErrorMessage
import com.home.piperbike.i18n.Localization
import java.util.UUID

class UserActivityNotFoundByIdException(id: UUID)
    : UserActivityNotFoundException(ErrorMessage(Localization.Error.USER_ACTIVITY_NOT_FOUND_BY_ID, listOf(id)))