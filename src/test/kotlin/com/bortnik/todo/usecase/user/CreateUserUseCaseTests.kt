package com.bortnik.todo.usecase.user

import com.bortnik.todo.domain.dto.user.UserCreate
import com.bortnik.todo.domain.entities.User
import com.bortnik.todo.domain.exceptions.user.UserAlreadyExists
import com.bortnik.todo.domain.repositories.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import kotlin.test.assertEquals

class CreateUserUseCaseTests {
    private val repo = mockk<UserRepository>()
    private val useCase = CreateUserUseCase(repo)

    @Test
    fun `save correct return new user`() {
        val username = "username"
        val email = "email@gmail.com"
        val password = "password"
        val hashedPassword = "hashedPassword"
        val userCreate = UserCreate(username, email, password)
        val savedUser = User(
            1,
            username,
            email,
            hashedPassword,
            LocalDateTime.now(),
            LocalDateTime.now()
        )

        every { repo.getByUsername(username) } returns null
        every { repo.getByEmail(email) } returns null
        every { repo.save(userCreate) } returns savedUser

        assertEquals(savedUser, useCase.save(userCreate))
        verify(exactly = 1) { repo.save(userCreate) }
    }

    @Test
    fun `save should throws UserAlreadyExists by username`() {
        val username = "username"
        val email = "email@gmail.com"
        val password = "password"
        val hashedPassword = "hashedPassword"
        val userCreate = UserCreate(username, email, password)
        val existsUser = User(
            1,
            username,
            email,
            hashedPassword,
            LocalDateTime.now(),
            LocalDateTime.now()
        )

        every { repo.getByUsername(username) } returns existsUser

        val exception = assertThrows<UserAlreadyExists> {
            useCase.save(userCreate)
        }

        assertEquals("User with username '${username}' already exists", exception.message)

        verify(exactly = 0) { repo.save(userCreate) }
    }

    @Test
    fun `save should throws UserAlreadyExists by email`() {
        val username = "username"
        val email = "email@gmail.com"
        val password = "password"
        val hashedPassword = "hashedPassword"
        val userCreate = UserCreate(username, email, password)
        val existsUser = User(
            1,
            username,
            email,
            hashedPassword,
            LocalDateTime.now(),
            LocalDateTime.now()
        )

        every { repo.getByUsername(username) } returns null
        every { repo.getByEmail(email) } returns existsUser


        val exception = assertThrows<UserAlreadyExists> {
            useCase.save(userCreate)
        }

        assertEquals("User with email '$email' already exists", exception.message)

        verify(exactly = 0) { repo.save(userCreate) }
    }
}