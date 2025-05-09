package com.bortnik.todo.api.http.controllers

import com.bortnik.todo.api.http.exceptions.BadCredentials
import com.bortnik.todo.api.http.openapi.controllers.AuthApiDocs
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
): AuthApiDocs {

    @PostMapping("/register")
    override fun register(@RequestBody user: UserCreate): AuthResponse {
        if (user.username.length < 3 || user.username.length > 64) {
            throw BadCredentials("username is too long or short")
        }
        user.email?.let { email ->
            if(email.length < 5 || email.length > 64) {
                throw BadCredentials("email is too long or short")
            }
            if(!Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(email)) {
                throw BadCredentials("incorrect email")
            }
        }
        if (user.password.length < 8) {
            throw BadCredentials("password length must be greater or equal to 8")
        }
        return authService.register(user)
    }

    @PostMapping("/login")
    override fun authenticate(@RequestBody user: UserLogin): AuthResponse {
        if (user.username.length < 3 || user.username.length > 64) {
            throw BadCredentials("username is too long or short")
        }
        if (user.password.length < 8) {
            throw BadCredentials("password length must be greater or equal to 8")
        }
        return authService.authentication(user)
    }
}