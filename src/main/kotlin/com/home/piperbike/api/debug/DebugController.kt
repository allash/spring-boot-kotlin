package com.home.piperbike.api.debug

import com.home.piperbike.api.BaseController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/debug")
class DebugController : BaseController() {

    @GetMapping("/ping")
    fun getPong(): String {
        return "pong"
    }
}