package com.bortnik.todo.api.http.controllers

import com.bortnik.todo.api.http.exceptions.BadCredentials
import com.bortnik.todo.api.http.openapi.controllers.AuthApiDocs
import com.bortnik.todo.api.http.validators.EmailValidator
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
        validateUserLogin(user.username, user.password)
        user.email?.let { email ->
            if(email.length < 5 || email.length > 254) {
                throw BadCredentials("email is too long or short")
            }
            if(!isEmail(email)) {
                throw BadCredentials("incorrect email")
            }
        }
        return authService.register(user)
    }

    @PostMapping("/login")
    override fun authenticate(@RequestBody user: UserLogin): AuthResponse {
        validateUserLogin(user.username, user.password)
        return authService.authentication(user)
    }

    private fun isEmail(field: String): Boolean {
        return EmailValidator.isValid(field)
    }

    private fun validateUserLogin(username: String, password: String) {
        if (username.length < 3 || username.length > 64) {
            throw BadCredentials("username is too long or short")
        }
        if (!isEmail(username) && !Regex("^[A-Za-z0-9_]+$").matches(username)) {
            throw BadCredentials("username can contains only latin characters, digits, and '_'")
        }
        if (password.length < 8) {
            throw BadCredentials("password length must be greater or equal to 8")
        }
    }
}