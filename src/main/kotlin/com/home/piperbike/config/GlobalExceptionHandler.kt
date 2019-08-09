package com.home.piperbike.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.home.piperbike.api.shared.exception.EntityNotFoundException
import com.home.piperbike.api.shared.exception.ServiceException
import com.home.piperbike.api.shared.dto.DtoError
import com.home.piperbike.api.shared.exception.auth.LoginRequiredException
import com.home.piperbike.api.shared.exception.auth.UserAccessDeniedException
import com.home.piperbike.api.shared.i18n.ErrorMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.HandlerExceptionResolver
import java.lang.reflect.UndeclaredThrowableException
import java.util.concurrent.ExecutionException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.reflect.jvm.javaMethod

@ControllerAdvice
class GlobalExceptionHandler @Autowired constructor(
        private val objectMapper: ObjectMapper,
        private val errorResolver: HandlerExceptionResolver
) {

    // 400 - BAD REQUEST

    @ExceptionHandler(value = [
        AccessDeniedException::class
    ])
    fun handleForbiddenMessageOnly(ex: Throwable, response: HttpServletResponse) {
        sendErrorResponse(ex, response, HttpStatus.FORBIDDEN, ErrorLogType.MESSAGE_ONLY)
    }

    // 404 - NOT FOUND

    @ExceptionHandler(value = [
        EntityNotFoundException::class
    ])
    fun handleNotFound(ex: Throwable, response: HttpServletResponse) {
        sendErrorResponse(ex, response, HttpStatus.NOT_FOUND)
    }

    // 401 - UNAUTHORIZED

    @ExceptionHandler(value = [
        LoginRequiredException::class
    ])
    fun handleUnauthorizedMessageOnly(ex: Throwable, response: HttpServletResponse) {
        sendErrorResponse(ex, response, HttpStatus.UNAUTHORIZED, ErrorLogType.MESSAGE_ONLY)
    }

    // 403 - FORBIDDEN

    @ExceptionHandler(value = [
        UserAccessDeniedException::class
    ])
    fun handleForbidden(ex: Throwable, response: HttpServletResponse) {
        sendErrorResponse(ex, response, HttpStatus.FORBIDDEN)
    }

    // EDGE CASES

    @ExceptionHandler(value = [
        ExecutionException::class,
        UndeclaredThrowableException::class
    ])
    fun handleWrappedException(ex: Throwable, request: HttpServletRequest, response: HttpServletResponse) {
        handleExceptionCause(ex, request, response)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException, response: HttpServletResponse) {
        val status = HttpStatus.UNPROCESSABLE_ENTITY
        val fieldErrors = ex.bindingResult.fieldErrors.map { error ->
            error.field to error.defaultMessage
        }.toMap()
        return sendErrorResponse(ex, response, status,
                DtoError(ErrorMessage(
                        message = "validation_failed", fieldErrors = fieldErrors
                ), status), logType = ErrorLogType.MESSAGE_ONLY)
    }

    // UNCAUGHT EXCEPTION FALLBACK

    @ExceptionHandler(Throwable::class)
    fun handleUncaughtException(ex: Throwable, response: HttpServletResponse) {
        sendErrorResponse(ex, response, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    // HELPERS

    companion object {
        private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)!!
        private val EXCEPTION_WRAPPERS = GlobalExceptionHandler::handleWrappedException.javaMethod!!
                .getDeclaredAnnotation(ExceptionHandler::class.java).value.toList()
    }

    private enum class ErrorLogType {
        STACK_TRACE,
        MESSAGE_ONLY
    }

    private fun getMessageOf(ex: Throwable): ErrorMessage {
        return if (ex is ServiceException) ex.i18nMessage
        else ErrorMessage("unknown_error", listOf(ex.message ?: ex::class.simpleName ?: "Unknown error"))
    }

    private fun sendErrorResponse(ex: Throwable? = null, response: HttpServletResponse, status: HttpStatus, body: Any, logType: ErrorLogType = ErrorLogType.STACK_TRACE) {
        val json = objectMapper.writeValueAsString(body)

        if (ex != null) {
            val prefix = "Global error caught:"
            when (logType) {
                ErrorLogType.STACK_TRACE -> logger.error(prefix, ex)
                ErrorLogType.MESSAGE_ONLY -> logger.error("$prefix ${ex.message}")
            }
        } else {
            logger.error("Global error caught:\n$json")
        }
        response.status = status.value()
        response.contentType = "application/json;charset=UTF-8"
        response.writer.use { writer ->
            writer.print(json)
        }
    }

    private fun isWrappedException(ex: Throwable): Boolean {
        return EXCEPTION_WRAPPERS.any { wrapper -> wrapper.isInstance(ex) }
    }

    private fun sendErrorResponse(ex: Throwable, response: HttpServletResponse, status: HttpStatus, logType: ErrorLogType = ErrorLogType.STACK_TRACE) {
        sendErrorResponse(ex, response, status, DtoError(getMessageOf(ex), status.value()), logType)
    }

    private fun handleExceptionCause(ex: Throwable, request: HttpServletRequest, response: HttpServletResponse) {
        val cause = ex.cause ?: return sendErrorResponse(ex, response, HttpStatus.INTERNAL_SERVER_ERROR)
        when {
            cause !is Exception -> handleUncaughtException(cause, response)
            isWrappedException(cause) -> handleExceptionCause(cause, request, response)
            else -> errorResolver.resolveException(request, response, null, cause)
        }
    }
}
