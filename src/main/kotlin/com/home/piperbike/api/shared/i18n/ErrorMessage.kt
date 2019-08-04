package com.home.piperbike.api.shared.i18n

import com.fasterxml.jackson.databind.ObjectMapper

class ErrorMessage(val message: String, params: List<Any> = emptyList(), val fieldErrors: Map<String, String?> = emptyMap(), errorCode: String? = null) {

    val params = params.map { it.toString() }

    val errorCode: String = errorCode?.toUpperCase() ?: message.toUpperCase()

    companion object {
        private val objectMapper = ObjectMapper()
    }

    override fun toString(): String {
        return objectMapper.writeValueAsString(this)
    }
}