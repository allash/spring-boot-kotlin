package com.home.piperbike.api.auth

import com.home.piperbike.api.BaseController
import com.home.piperbike.api.auth.dto.request.DtoLoginRequest
import com.home.piperbike.api.auth.dto.response.DtoLoginResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/auth")
class AuthController @Autowired constructor(
        private val authService: AuthService
) : BaseController() {

    @PostMapping("/login")
    fun login(@Valid @RequestBody body: DtoLoginRequest): DtoLoginResponse = authService.login(body)
}