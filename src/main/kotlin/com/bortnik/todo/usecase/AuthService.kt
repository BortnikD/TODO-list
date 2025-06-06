package com.bortnik.todo.usecase

import com.bortnik.todo.api.http.validators.user.EmailValidator
import com.bortnik.todo.domain.dto.user.UserCreate
import com.bortnik.todo.domain.dto.user.AuthResponse
import com.bortnik.todo.domain.dto.user.UserLogin
import com.bortnik.todo.domain.exceptions.user.UserNotFound
import com.bortnik.todo.infrastructure.security.JwtUtil
import com.bortnik.todo.usecase.user.CreateUserUseCase
import com.bortnik.todo.usecase.user.GetUserUseCase
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val createUserUseCase: CreateUserUseCase,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val getUserUseCase: GetUserUseCase
) {

    fun register(user: UserCreate): AuthResponse {
        val savedUser = createUserUseCase.save(
            user.copy(password = passwordEncoder.encode(user.password))
        )
        val userDetails = userDetailsService.loadUserByUsername(savedUser.username)
        val token = jwtUtil.generateToken(userDetails)

        return AuthResponse(token, savedUser.username)
    }

    fun authentication(user: UserLogin): AuthResponse {
        val username = if (EmailValidator.isValid(user.username)) {
            getUserUseCase.getByEmail(user.username)?.username
                ?: throw UserNotFound("user with this email '${user.username}' does not exists")
        } else {
            getUserUseCase.getByUsername(user.username)?.username
                ?: throw UserNotFound("Not found user to login with username '${user.username}'")
        }

        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(username, user.password)
        )
        val userDetails = userDetailsService.loadUserByUsername(username)
        val token = jwtUtil.generateToken(userDetails)

        return AuthResponse(token, username)
    }
}