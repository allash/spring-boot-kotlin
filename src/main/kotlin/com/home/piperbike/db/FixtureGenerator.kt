package com.home.piperbike.db

import com.github.javafaker.Faker
import com.home.piperbike.db.entities.DbActivity
import com.home.piperbike.db.entities.DbSession
import com.home.piperbike.db.entities.DbUser
import com.home.piperbike.db.repositories.ActivityRepository
import com.home.piperbike.db.repositories.SessionRepository
import com.home.piperbike.db.repositories.UserRepository
import com.home.piperbike.helper.PasswordHelper
import com.home.piperbike.removeHyphens
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.*

@Component
@Profile("load-fixtures", "test")
class FixtureGenerator @Autowired constructor(
        private val userRepo: UserRepository,
        private val sessionRepo: SessionRepository,
        private val activityRepository: ActivityRepository,
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

    fun createSession(
            user: DbUser
    ): DbSession {
        return DbSession().let { session ->
            session.user = user
            session.userId = user.id
            session.token = UUID.randomUUID().removeHyphens()
            sessionRepo.save(session)
        }
    }

    fun createActivity(
            name: String = faker.food().spice(),
            description: String? = faker.lorem().paragraph(100),
            distance: Float? = faker.number().numberBetween(1, 1000).toFloat(),
            elapsedTime: Int? = faker.number().numberBetween(100, 10000),
            user: DbUser
    ): DbActivity {
        return DbActivity().let { activity ->
            activity.name = name
            activity.description = description
            activity.distance = distance
            activity.elapsedTime = elapsedTime
            activity.user = user
            activity.userId = user.id
            activityRepository.save(activity)
        }
    }
}