package com.bortnik.todo.usecase

import com.bortnik.todo.domain.dto.user.UserCreate
import com.bortnik.todo.domain.dto.user.UserLogin
import com.bortnik.todo.domain.entities.User
import com.bortnik.todo.domain.exceptions.user.UserNotFound
import com.bortnik.todo.infrastructure.security.JwtUtil
import com.bortnik.todo.usecase.user.CreateUserUseCase
import com.bortnik.todo.usecase.user.GetUserUseCase
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import kotlin.test.assertEquals

class AuthServiceTests {

    private val createUserUseCase = mockk<CreateUserUseCase>()
    private val passwordEncoder = mockk<PasswordEncoder>()
    private val jwtUtil = mockk<JwtUtil>()
    private val authenticationManager = mockk<AuthenticationManager>()
    private val userDetailsService = mockk<UserDetailsService>()
    private val getUserUseCase = mockk<GetUserUseCase>()
    private val authService = AuthService(
        createUserUseCase,
        passwordEncoder,
        jwtUtil,
        authenticationManager,
        userDetailsService,
        getUserUseCase
    )

    @Test
    fun `register should return auth response with token and username when registration successful`() {
        val username = "username"
        val email = "email@gmail.com"
        val password = "password"
        val encodedPassword = "hashedPassword"
        val jwtToken = "secretToken"

        val userCreate = UserCreate(
            username = username,
            email = email,
            password = password
        )
        val userCreateEncoded = UserCreate(
            username = username,
            email = email,
            password = encodedPassword
        )
        val savedUser = User(
            1,
            username,
            email,
            encodedPassword,
            LocalDateTime.now(),
            LocalDateTime.now()
        )
        val userDetails = org.springframework.security.core.userdetails.User(
            username, encodedPassword, listOf()
        )

        every { createUserUseCase.save(userCreateEncoded) } returns savedUser
        every { passwordEncoder.encode(password) } returns encodedPassword
        every { userDetailsService.loadUserByUsername(username) } returns userDetails
        every { jwtUtil.generateToken(userDetails) } returns jwtToken

        val authResponse = authService.register(userCreate)
        assertEquals(jwtToken, authResponse.token)
        assertEquals(username, authResponse.username)
    }

    @Test
    fun `authentication should return response with token and username by username`() {
        val username = "username"
        val email = "email@gmail.com"
        val password = "password"
        val encodedPassword = "hashedPassword"
        val jwtToken = "secretToken"
        val user = User(
            1,
            username,
            email,
            encodedPassword,
            LocalDateTime.now(),
            LocalDateTime.now()
        )
        val userDetails = org.springframework.security.core.userdetails.User(
            username, encodedPassword, listOf()
        )
        val userLogin = UserLogin(
            username = username,
            password = password
        )

        every { getUserUseCase.getByEmail(email) } returns null
        every { getUserUseCase.getByUsername(username) } returns user
        every { authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(username, password))
        } returns mockk<Authentication>()
        every { userDetailsService.loadUserByUsername(username) } returns userDetails
        every { jwtUtil.generateToken(userDetails) } returns jwtToken

        val authResponse = authService.authentication(userLogin)
        assertEquals(jwtToken, authResponse.token)
        assertEquals(username, authResponse.username)
    }

    @Test
    fun `authentication should return response with token and username by email`() {
        val username = "username"
        val email = "email@gmail.com"
        val password = "password"
        val encodedPassword = "hashedPassword"
        val jwtToken = "secretToken"
        val user = User(
            1,
            username,
            email,
            encodedPassword,
            LocalDateTime.now(),
            LocalDateTime.now()
        )
        val userDetails = org.springframework.security.core.userdetails.User(
            username, encodedPassword, listOf()
        )
        val userLogin = UserLogin(
            username = email,
            password = password
        )

        every { getUserUseCase.getByEmail(email) } returns user
        every { getUserUseCase.getByUsername(username) } returns null
        every { authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(username, password))
        } returns mockk<Authentication>()
        every { userDetailsService.loadUserByUsername(username) } returns userDetails
        every { jwtUtil.generateToken(userDetails) } returns jwtToken

        val authResponse = authService.authentication(userLogin)
        assertEquals(jwtToken, authResponse.token)
        assertEquals(username, authResponse.username)
    }

    @Test
    fun `authentication should throws UserNotFound with not exists email`() {
        val email = "email@gmail.com"
        val password = "password"

        val userLogin = UserLogin(
            username = email,
            password = password
        )

        every { getUserUseCase.getByEmail(email) } returns null

        val exception = assertThrows<UserNotFound> {
            authService.authentication(userLogin)
        }
        assertEquals("user with this email '${email}' does not exists", exception.message)
    }

    @Test
    fun `authentication should throws UserNotFound with not exists username`() {
        val username = "username"
        val password = "password"

        val userLogin = UserLogin(
            username = username,
            password = password
        )

        every { getUserUseCase.getByUsername(username) } returns null

        val exception = assertThrows<UserNotFound> {
            authService.authentication(userLogin)
        }
        assertEquals("Not found user to login with username '${username}'", exception.message)
    }
}