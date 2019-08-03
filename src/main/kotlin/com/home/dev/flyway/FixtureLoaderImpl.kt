package com.home.dev.flyway

import com.home.piperbike.db.FixtureGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Profile("load-fixtures")
@Component
class FixtureLoaderImpl @Autowired constructor(
        private val fixtures: FixtureGenerator
) : FixtureLoader {

    @Transactional(rollbackFor = [Throwable::class], readOnly = false)
    override fun load() {

        fixtures.createUser(
                firstName = "John",
                lastName = "Doe",
                email = "john.doe@mail.com",
                password = "asdf"
        )
    }
}