package com.home.piperbike

class Extensions

/**
 * Wraps a code block within a try/catch and calls catchException if an exception occurs.
 * @return The result of `block` if no error occurs. The result of `catchException` otherwise.
 */
inline fun <T, R> T.tryCatch(block: (T) -> R, catchException: (Throwable) -> R): R {
    return try {
        block(this)
    } catch (e: Throwable) {
        catchException(e)
    }
}

/**
 * Wraps the code block within a try/catch and returns null if an exception occurs.
 * @return The result of `block` if no error occurs. `null` otherwise.
 */
inline fun <T, R> T.tryOrNull(block: (T) -> R): R? = this.tryCatch(block) { null }
