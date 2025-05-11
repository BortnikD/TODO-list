package com.bortnik.todo.usecase.user

import com.bortnik.todo.domain.entities.User
import com.bortnik.todo.domain.repositories.UserRepository
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetUserUseCaseTests {

    private val repo = mockk<UserRepository>()
    private val useCase = GetUserUseCase(repo)

    @Test
    fun `geById should return user`() {
        val userId = 1
        val user = User(
            userId,
            "username",
            "email",
            "hashedPassword",
            LocalDateTime.now(),
            LocalDateTime.now()
        )

        every { repo.getById(userId) } returns user

        assertEquals(user, useCase.getById(userId))
    }

    @Test
    fun `geById should return null`() {
        val userId = 1

        every { repo.getById(userId) } returns null

        assertNull(useCase.getById(1), "user must be null")
    }

    @Test
    fun `geByUsername should return user`() {
        val username = "username"
        val user = User(
            1,
            username,
            "email",
            "hashedPassword",
            LocalDateTime.now(),
            LocalDateTime.now()
        )

        every { repo.getByUsername(username) } returns user

        assertEquals(user, useCase.getByUsername(username))
    }

    @Test
    fun `getByUsername should return null`() {
        val userUsername = "username"

        every { repo.getByUsername(userUsername) } returns null

        assertNull(useCase.getByUsername(userUsername), "user must be null")
    }

    @Test
    fun `geByEmail should return user`() {
        val email = "email"
        val user = User(
            1,
            "username",
            email,
            "hashedPassword",
            LocalDateTime.now(),
            LocalDateTime.now()
        )

        every { repo.getByEmail(email) } returns user

        assertEquals(user, useCase.getByEmail(email))
    }
}