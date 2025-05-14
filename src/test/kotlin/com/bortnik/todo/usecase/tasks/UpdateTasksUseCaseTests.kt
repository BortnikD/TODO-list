package com.bortnik.todo.usecase.tasks

import com.bortnik.todo.domain.dto.TaskUpdate
import com.bortnik.todo.domain.entities.Category
import com.bortnik.todo.domain.entities.Task
import com.bortnik.todo.domain.exceptions.category.CategoryNotFound
import com.bortnik.todo.domain.exceptions.task.TaskNotFound
import com.bortnik.todo.domain.exceptions.auth.AcceptError
import com.bortnik.todo.domain.repositories.TaskRepository
import com.bortnik.todo.usecase.category.GetCategoryUseCase
import com.bortnik.todo.usecase.task.UpdateTaskUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class UpdateTasksUseCaseTests {
    private val repo = mockk<TaskRepository>()
    private val getCategoryUseCase = mockk<GetCategoryUseCase>()
    private val useCase = UpdateTaskUseCase(repo, getCategoryUseCase)

    @Test
    fun `updateTask should throws CategoryNotFound when category doesn't exists`() {
        val taskId = 1
        val categoryId = 1
        val priority = 1
        val text = "text"
        val taskUpdate = TaskUpdate(taskId, categoryId, priority, text)

        every { getCategoryUseCase.getCategoryById(categoryId) } returns null

        assertThrows<CategoryNotFound> {
            useCase.updateTask(taskUpdate, 1)
        }

        verify(exactly = 1) { getCategoryUseCase.getCategoryById(categoryId) }
        verify(exactly = 0) { repo.updateTask(taskUpdate) }
    }

    @Test
    fun `updateTask should throws TaskNotFound when task doesn't exists`() {
        val userId = 1
        val taskId = 1
        val categoryId = 1
        val priority = 1
        val text = "text"
        val taskUpdate = TaskUpdate(taskId, categoryId, priority, text)

        every { getCategoryUseCase.getCategoryById(categoryId) } returns Category(categoryId, userId, "name")
        every { repo.getTaskById(taskId) } returns null

        assertThrows<TaskNotFound> {
            useCase.updateTask(taskUpdate, userId)
        }

        verify(exactly = 1) { getCategoryUseCase.getCategoryById(categoryId) }
        verify(exactly = 1) { repo.getTaskById(taskId) }
        verify(exactly = 0) { repo.updateTask(taskUpdate) }
    }

    @Test
    fun `updateTask should throws AcceptError when user is not author doesn't exists`() {
        val userId = 1
        val anotherUserId = 2
        val taskId = 1
        val categoryId = 1
        val priority = 1
        val text = "text"
        val taskUpdate = TaskUpdate(taskId, categoryId, priority, text)

        every { getCategoryUseCase.getCategoryById(categoryId) } returns Category(categoryId, userId, "name")
        every { repo.getTaskById(taskId) } returns Task(
            taskId,
            categoryId,
            anotherUserId,
            priority,
            text
        )

        assertThrows<AcceptError> {
            useCase.updateTask(taskUpdate, userId)
        }

        verify(exactly = 1) { getCategoryUseCase.getCategoryById(categoryId) }
        verify(exactly = 1) { repo.getTaskById(taskId) }
        verify(exactly = 0) { repo.updateTask(taskUpdate) }
    }

    @Test
    fun `updateTask should update task`() {
        val userId = 1
        val taskId = 1
        val categoryId = 1
        val priority = 1
        val text = "text"
        val taskUpdate = TaskUpdate(taskId, categoryId, priority, text)
        val resultTask = Task(
            taskId,
            categoryId,
            userId,
            priority,
            text
        )

        every { getCategoryUseCase.getCategoryById(categoryId) } returns Category(categoryId, userId, "name")
        every { repo.getTaskById(taskId) } returns resultTask
        every { repo.updateTask(taskUpdate) } returns resultTask

        assertEquals(useCase.updateTask(taskUpdate, userId), resultTask)

        verifyOrder {
            getCategoryUseCase.getCategoryById(categoryId)
            repo.getTaskById(taskId)
            repo.updateTask(taskUpdate)
        }
    }

    @Test
    fun `completeTask should throws TaskNotFound when task is not exists`() {
        val taskId = 1
        val userId = 1

        every { repo.getTaskById(taskId) } returns null

        assertThrows<TaskNotFound> {
            useCase.completeTask(taskId, userId)
        }

        verify(exactly = 1) { repo.getTaskById(taskId) }
        verify(exactly = 0) { repo.completeTask(taskId) }
    }

    @Test
    fun `completeTask should throws AcceptError when user is not author`() {
        val taskId = 1
        val userId = 1
        val anotherUserId = 2

        every { repo.getTaskById(taskId) } returns Task(
            taskId,
            1,
            anotherUserId,
            1,
            "text"
        )

        assertThrows<AcceptError> {
            useCase.completeTask(taskId, userId)
        }

        verify(exactly = 1) { repo.getTaskById(taskId) }
        verify(exactly = 0) { repo.completeTask(taskId) }
    }

    @Test
    fun `completeTask should complete task`() {
        val userId = 1
        val taskId = 1
        val categoryId = 1
        val priority = 1
        val text = "text"
        val resultTask = Task(
            taskId,
            categoryId,
            userId,
            priority,
            text
        )

        every { repo.getTaskById(taskId) } returns resultTask
        every { repo.completeTask(taskId) } returns Unit

        useCase.completeTask(taskId, userId)

        verify(exactly = 1) { repo.getTaskById(taskId) }
        verify(exactly = 1) { repo.completeTask(taskId) }
    }
}