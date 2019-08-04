package com.home.piperbike.api.shared.dto

import com.home.piperbike.api.shared.i18n.ErrorMessage
import org.springframework.http.HttpStatus

data class DtoError(
        val error: ErrorMessage,
        val status: Int
) {
    constructor(message: ErrorMessage, status: HttpStatus) : this(message, status.value())
}