package com.bortnik.todo.usecase.tasks

import com.bortnik.todo.domain.dto.TaskCreate
import com.bortnik.todo.domain.entities.Category
import com.bortnik.todo.domain.entities.Task
import com.bortnik.todo.domain.exceptions.category.CategoryNotFound
import com.bortnik.todo.domain.exceptions.auth.AcceptError
import com.bortnik.todo.domain.repositories.TaskRepository
import com.bortnik.todo.usecase.category.GetCategoryUseCase
import com.bortnik.todo.usecase.task.CreateTaskUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateTasksUseCaseTests {
    private val repo = mockk<TaskRepository>()
    private val getCategoryUseCase = mockk<GetCategoryUseCase>()
    private val useCase = CreateTaskUseCase(repo, getCategoryUseCase)

    @Test
    fun `addTask should throws CategoryNotFound if category doesn't exists`() {
        val userId = 1
        val categoryId = 1
        val priority = 1
        val text = "text"
        val taskCreate = TaskCreate(userId, categoryId, priority, text)

        every { getCategoryUseCase.getCategoryById(categoryId) } returns null

        assertThrows<CategoryNotFound> {
            useCase.addTask(taskCreate)
        }

        verify(exactly = 1) { getCategoryUseCase.getCategoryById(categoryId) }
        verify(exactly = 0) { repo.addTask(any()) }
    }

    @Test
    fun `addTask should throws AcceptError if category doesn't exists`() {
        val userId = 1
        val anotherUserId = 2
        val categoryId = 1
        val priority = 1
        val text = "text"
        val taskCreate = TaskCreate(userId, categoryId, priority, text)

        every { getCategoryUseCase.getCategoryById(categoryId) } returns Category(1, anotherUserId, "name")

        assertThrows<AcceptError> {
            useCase.addTask(taskCreate)
        }

        verify(exactly = 1) { getCategoryUseCase.getCategoryById(categoryId) }
        verify(exactly = 0) { repo.addTask(any()) }
    }

    @Test
    fun `addTask should create task if category exists`() {
        val userId = 1
        val categoryId = 1
        val priority = 1
        val text = "text"
        val taskCreate = TaskCreate(userId, categoryId, priority, text)
        val resultTask = Task(1, userId, categoryId, priority, text)

        every { getCategoryUseCase.getCategoryById(categoryId) } returns Category(1, userId, "name")
        every { repo.addTask(taskCreate) } returns resultTask

        assertEquals(useCase.addTask(taskCreate), resultTask)

        verifyOrder {
            getCategoryUseCase.getCategoryById(categoryId)
            repo.addTask(any())
        }
    }
}