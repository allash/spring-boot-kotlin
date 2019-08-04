package com.home.piperbike.helper

import com.home.piperbike.tryCatch
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Component
import java.security.SecureRandom

@Component
class PasswordHelper {

    companion object {
        private const val GENSALT_DEFAULT_LOG2_ROUNDS = 10
        private val SECURE_RANDOM = SecureRandom()
    }

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt(GENSALT_DEFAULT_LOG2_ROUNDS, SECURE_RANDOM))
    }

    fun checkPassword(password: String, passwordHash: String): Boolean =
            tryCatch({ BCrypt.checkpw(password, passwordHash) }, { false })
}