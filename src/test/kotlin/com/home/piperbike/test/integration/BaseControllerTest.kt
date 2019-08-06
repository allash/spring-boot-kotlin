package com.home.piperbike.test.integration

import com.home.piperbike.test.BaseSpringTest
import org.junit.Before
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import kotlin.reflect.KFunction

abstract class BaseControllerTest(val testTarget: KFunction<*>) : BaseSpringTest() {

    protected lateinit var mvc: MockMvc

    @Before
    override fun setUp() {
        super.setUp()
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
                .build()
    }
}