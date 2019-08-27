package com.home.piperbike.api.auth

import com.home.piperbike.api.BaseController
import com.home.piperbike.api.auth.dto.request.DtoAuthLoginRequest
import com.home.piperbike.api.auth.dto.request.DtoAuthRegisterRequest
import com.home.piperbike.api.auth.dto.response.DtoLoginResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/auth")
class AuthController @Autowired constructor(
        private val authService: AuthService
) : BaseController() {

    @PostMapping("/login")
    fun login(@Valid @RequestBody body: DtoAuthLoginRequest): DtoLoginResponse = authService.login(body)

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    fun register(@Valid @RequestBody body: DtoAuthRegisterRequest) = authService.register(body)
}