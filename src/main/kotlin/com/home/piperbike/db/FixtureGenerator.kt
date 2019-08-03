package com.home.piperbike.db

import com.github.javafaker.Faker
import com.home.piperbike.db.entities.DbUser
import com.home.piperbike.db.repositories.UserRepository
import com.home.piperbike.helpers.PasswordHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.*

@Component
@Profile("load-fixtures", "test")
class FixtureGenerator @Autowired constructor(
        private val userRepo: UserRepository,
        private val passwordHelper: PasswordHelper
) {

    companion object {
        private val SEED = Random(123456789)
        private val faker = Faker(Locale.GERMAN, SEED)
    }

    fun createUser(
            firstName: String = faker.name().firstName(),
            lastName: String = faker.name().lastName(),
            email: String = "$firstName.$lastName@mail.com",
            password: String = "asdf"
    ): DbUser {
        return DbUser().let { user ->
            user.email = email
            user.firstName = firstName
            user.lastName = lastName
            user.password = passwordHelper.hashPassword(password)
            userRepo.save(user)
        }
    }
}