package com.bortnik.todo.api.http.controllers

import com.bortnik.todo.domain.dto.user.AuthResponse
import com.bortnik.todo.domain.dto.user.UserCreate
import com.bortnik.todo.domain.dto.user.UserLogin
import com.bortnik.todo.usecase.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(@RequestBody user: UserCreate): AuthResponse {
        return authService.register(user)
    }

    @PostMapping("/login")
    fun authenticate(@RequestBody user: UserLogin): AuthResponse {
        return authService.authentication(user)
    }
}