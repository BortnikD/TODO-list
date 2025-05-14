package com.bortnik.todo.api

import com.bortnik.todo.api.http.controllers.AuthController
import com.bortnik.todo.api.http.exceptions.BadCredentials
import com.bortnik.todo.domain.dto.user.AuthResponse
import com.bortnik.todo.domain.dto.user.UserCreate
import com.bortnik.todo.domain.dto.user.UserLogin
import com.bortnik.todo.usecase.AuthService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class AuthControllerTests {

    private val authService = mockk<AuthService>()
    private val controller = AuthController(authService)

    private val validUserCreate = UserCreate(
        username = "username",
        password = "password123",
        email = "madamada@example.com"
    )

    private val validUserLogin = UserLogin(
        username = "username",
        password = "password123"
    )

    private val authResponse = AuthResponse(
        token = "test-token",
        username = "username"
    )

    @Test
    fun `register should throws BadCredentials when username length is incorrect`(){
        val user = validUserCreate.copy(username = "us")

        val exception = assertThrows<BadCredentials> {
            controller.register(user)
        }

        assertEquals("username is too long or short", exception.message)
    }

    @Test
    fun `register should throws BadCredentials when username is not email and contains wrong characters`() {
        // username contains '-' is not correct
        val user = validUserCreate.copy(username = "general-Kenobi")

        val exception = assertThrows<BadCredentials> {
            controller.register(user)
        }

        assertEquals("username can contains only latin characters, digits, and '_'", exception.message)
    }

    @Test
    fun `register should throws BadCredentials when password length less than 8`() {
        val user = validUserCreate.copy(password = "1234567")

        val exception = assertThrows<BadCredentials> {
            controller.register(user)
        }

        assertEquals("password length must be greater or equal to 8", exception.message)
    }

    @Test
    fun `register should throws BadCredentials when email is incorrect`() {
        val invalidEmails = listOf(
            "wrongEmail",
            "wrong@email",
            "wrong@email.",
            "@email.com",
            "user..name@example.com",
            "user@-example.com",
            "user@.com"
        )

        invalidEmails
            .map { email -> validUserCreate.copy(email = email) }
            .forEach { user ->
                val exception = assertThrows<BadCredentials> { controller.register(user) }
                assertEquals("incorrect email", exception.message)
            }
    }

    @Test
    fun `register should return auth response when credentials are valid`() {
        every { authService.register(validUserCreate) } returns authResponse
        assertEquals(authResponse, controller.register(validUserCreate))
    }

    @Test
    fun `authenticate should throws BadCredentials when username length is incorrect`(){
        val user = validUserLogin.copy(username = "us")

        val exception = assertThrows<BadCredentials> {
            controller.authenticate(user)
        }

        assertEquals("username is too long or short", exception.message)
    }

    @Test
    fun `authenticate should throws BadCredentials when username is not email and contains wrong characters`() {
        // username contains '-' is not correct
        val user = validUserLogin.copy(username = "general-Kenobi")

        val exception = assertThrows<BadCredentials> {
            controller.authenticate(user)
        }

        assertEquals("username can contains only latin characters, digits, and '_'", exception.message)
    }

    @Test
    fun `authenticate should throws BadCredentials when password length less than 8`() {
        val user = validUserLogin.copy(password = "1234567")

        val exception = assertThrows<BadCredentials> {
            controller.authenticate(user)
        }

        assertEquals("password length must be greater or equal to 8", exception.message)
    }

    @Test
    fun `authenticate should return auth response when credentials are valid`() {
        every { authService.authentication(validUserLogin) } returns authResponse
        assertEquals(authResponse, controller.authenticate(validUserLogin))
    }
}