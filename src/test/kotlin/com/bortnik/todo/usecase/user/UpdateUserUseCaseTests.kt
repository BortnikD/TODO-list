package com.bortnik.todo.usecase.user

import com.bortnik.todo.domain.dto.user.UserUpdate
import com.bortnik.todo.domain.entities.User
import com.bortnik.todo.domain.exceptions.user.UserAlreadyExists
import com.bortnik.todo.domain.repositories.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import kotlin.test.assertEquals

class UpdateUserUseCaseTests {

    private val repo = mockk<UserRepository>()
    private val useCase = UpdateUserUseCase(repo)

    @Test
    fun `update should update user`() {
        val userId = 1
        val username = "username"
        val email = "email@gmail.com"
        val userUpdate = UserUpdate(userId, username, email)
        val user = User(
            userId,
            username,
            email,
            "hashedPassword",
            LocalDateTime.now(),
            LocalDateTime.now()
        )

        every { repo.getByUsername(username) } returns null
        every { repo.getByEmail(email) } returns null
        every { repo.update(userUpdate) } returns user

        assertEquals(user, useCase.update(userUpdate))
    }

    @Test
    fun `update should throws UserAlreadyExists by username`() {
        val userId = 1
        val username = "username"
        val email = "email@gmail.com"
        val userUpdate = UserUpdate(userId, username, email)
        val user = User(
            userId,
            username,
            email,
            "hashedPassword",
            LocalDateTime.now(),
            LocalDateTime.now()
        )

        every { repo.getByUsername(username) } returns user

        val exception = assertThrows<UserAlreadyExists> {
            useCase.update(userUpdate)
        }

        assertEquals("Sorry, '${username}' is already in use. " +
                "Please pick another username.", exception.message)
    }

    @Test
    fun `update should throws UserAlreadyExists by email`() {
        val userId = 1
        val username = "username"
        val email = "email@gmail.com"
        val userUpdate = UserUpdate(userId, username, email)
        val user = User(
            userId,
            username,
            email,
            "hashedPassword",
            LocalDateTime.now(),
            LocalDateTime.now()
        )

        every { repo.getByUsername(username) } returns null
        every { repo.getByEmail(email) } returns user

        val exception = assertThrows<UserAlreadyExists> {
            useCase.update(userUpdate)
        }

        assertEquals("Email: '${email}' is already in use.", exception.message)
    }
}