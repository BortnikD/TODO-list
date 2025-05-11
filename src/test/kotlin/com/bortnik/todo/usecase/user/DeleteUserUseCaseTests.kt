package com.bortnik.todo.usecase.user

import com.bortnik.todo.domain.repositories.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test

class DeleteUserUseCaseTests {
    private val repo = mockk<UserRepository>()
    private val useCase = DeleteUserUseCase(repo)

    @Test
    fun `deleteUser should delete user`() {
        val userId = 1
        every { repo.delete(userId) } returns Unit
        useCase.deleteUser(userId)
        verify(exactly = 1) { repo.delete(userId) }
    }
}