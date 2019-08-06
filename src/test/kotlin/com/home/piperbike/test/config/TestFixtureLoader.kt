package com.home.piperbike.test.config

import com.home.dev.flyway.FixtureLoader
import org.springframework.stereotype.Component

@Component
class TestFixtureLoader : FixtureLoader {
    override fun load() { }
}