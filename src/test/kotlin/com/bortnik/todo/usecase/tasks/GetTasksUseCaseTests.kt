package com.bortnik.todo.usecase.tasks

import com.bortnik.todo.domain.dto.PaginatedResponse
import com.bortnik.todo.domain.entities.Task
import com.bortnik.todo.domain.repositories.TaskRepository
import com.bortnik.todo.usecase.task.GetTaskUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GetTasksUseCaseTests {
    private val repo = mockk<TaskRepository>()
    private val baseUrl = "http://localhost:8080"
    private val useCase = GetTaskUseCase(repo, baseUrl)

    @Test
    fun `getPaginatedUncompletedTasks returns empty PaginatedResponse`() {
        val field = "priority"
        val userId = 1
        val offset = 0L
        val limit = 10

        every { repo.getTasksCountByUserId(userId, false) } returns 0
        every { repo.getTasksSortedByFieldOrDefault(field, offset, limit, userId) } returns listOf()

        val resultResponse = PaginatedResponse<Task>(
            count = 0,
            previousPage = null,
            nextPage = null,
            results = listOf()
        )

        assertEquals(resultResponse, useCase.getPaginatedUncompletedTasks(field, userId, offset, limit))
    }

    @Test
    fun `getPaginatedUncompletedTasks returns PaginatedResponse with nextPage`() {
        val field = "priority"
        val userId = 1
        val offset = 0L
        val limit = 10
        val size = 11L

        val taskList = (1..size).map { Task(it.toInt(), 1, userId, 1, "text $it") }
        val resultList = taskList.slice(offset.toInt()..<(offset.toInt() + limit))

        every { repo.getTasksCountByUserId(userId, false) } returns size
        every { repo.getTasksSortedByFieldOrDefault(field, offset, limit, userId) } returns resultList


        val resultResponse = PaginatedResponse(
            count = size,
            previousPage = null,
            nextPage = "$baseUrl/tasks?field=priority&offset=10&limit=10",
            results = resultList
        )

        assertEquals(resultResponse, useCase.getPaginatedUncompletedTasks(field, userId, offset, limit))
    }

    @Test
    fun `getPaginatedUncompletedTasks returns PaginatedResponse with previousPage`() {
        val field = "priority"
        val userId = 1
        val offset = 10L
        val limit = 10
        val size = 20L

        val taskList = (1..size).map { Task(it.toInt(), 1, userId, 1, "text $it") }
        val resultList = taskList.slice(offset.toInt()..<(offset.toInt() + limit))

        every { repo.getTasksCountByUserId(userId, false) } returns size
        every { repo.getTasksSortedByFieldOrDefault(field, offset, limit, userId) } returns resultList

        val resultResponse = PaginatedResponse(
            count = size,
            previousPage = "$baseUrl/tasks?field=priority&offset=0&limit=10",
            nextPage = null,
            results = resultList
        )

        assertEquals(resultResponse, useCase.getPaginatedUncompletedTasks(field, userId, offset, limit))
    }

    @Test
    fun `getPaginatedUncompletedTasks returns PaginatedResponse with previousPage and nextPage`() {
        val field = "priority"
        val userId = 1
        val offset = 10L
        val limit = 10
        val size = 30L

        val taskList = (1..size).map { Task(it.toInt(), 1, userId, 1, "text $it") }
        val resultList = taskList.slice(offset.toInt()..<(offset.toInt() + limit))

        every { repo.getTasksCountByUserId(userId, false) } returns size
        every { repo.getTasksSortedByFieldOrDefault(field, offset, limit, userId) } returns resultList

        val resultResponse = PaginatedResponse(
            count = size,
            previousPage = "$baseUrl/tasks?field=priority&offset=0&limit=10",
            nextPage = "$baseUrl/tasks?field=priority&offset=20&limit=10",
            results = resultList
        )

        assertEquals(resultResponse, useCase.getPaginatedUncompletedTasks(field, userId, offset, limit))
    }

    @Test
    fun `getPaginatedCompletedTasks returns empty PaginatedResponse`() {
        val field = "priority"
        val userId = 1
        val offset = 0L
        val limit = 10

        every { repo.getTasksCountByUserId(userId, true) } returns 0
        every { repo.getCompletedTasksSortedByFieldOrDefault(field, offset, limit, userId) } returns listOf()

        val resultResponse = PaginatedResponse<Task>(
            count = 0,
            previousPage = null,
            nextPage = null,
            results = listOf()
        )

        assertEquals(resultResponse, useCase.getPaginatedCompletedTasks(field, userId, offset, limit))
    }

    @Test
    fun `getPaginatedCompletedTasks returns PaginatedResponse with nextPage`() {
        val field = "priority"
        val userId = 1
        val offset = 0L
        val limit = 10
        val size = 11L

        val taskList = (1..size).map { Task(it.toInt(), 1, userId, 1, "text $it") }
        val resultList = taskList.slice(offset.toInt()..<(offset.toInt() + limit))

        every { repo.getTasksCountByUserId(userId, true) } returns size
        every { repo.getCompletedTasksSortedByFieldOrDefault(field, offset, limit, userId) } returns resultList


        val resultResponse = PaginatedResponse(
            count = size,
            previousPage = null,
            nextPage = "$baseUrl/tasks/completed?field=priority&offset=10&limit=10",
            results = resultList
        )

        assertEquals(resultResponse, useCase.getPaginatedCompletedTasks(field, userId, offset, limit))
    }

    @Test
    fun `getPaginatedCompletedTasks returns PaginatedResponse with previousPage`() {
        val field = "priority"
        val userId = 1
        val offset = 10L
        val limit = 10
        val size = 20L

        val taskList = (1..size).map { Task(it.toInt(), 1, userId, 1, "text $it") }
        val resultList = taskList.slice(offset.toInt()..<(offset.toInt() + limit))

        every { repo.getTasksCountByUserId(userId, true) } returns size
        every { repo.getCompletedTasksSortedByFieldOrDefault(field, offset, limit, userId) } returns resultList

        val resultResponse = PaginatedResponse(
            count = size,
            previousPage = "$baseUrl/tasks/completed?field=priority&offset=0&limit=10",
            nextPage = null,
            results = resultList
        )

        assertEquals(resultResponse, useCase.getPaginatedCompletedTasks(field, userId, offset, limit))
    }

    @Test
    fun `getPaginatedCompletedTasks returns PaginatedResponse with previousPage and nextPage`() {
        val field = "priority"
        val userId = 1
        val offset = 10L
        val limit = 10
        val size = 30L

        val taskList = (1..size).map { Task(it.toInt(), 1, userId, 1, "text $it") }
        val resultList = taskList.slice(offset.toInt()..<(offset.toInt() + limit))

        every { repo.getTasksCountByUserId(userId, true) } returns size
        every { repo.getCompletedTasksSortedByFieldOrDefault(field, offset, limit, userId) } returns resultList

        val resultResponse = PaginatedResponse(
            count = size,
            previousPage = "$baseUrl/tasks/completed?field=priority&offset=0&limit=10",
            nextPage = "$baseUrl/tasks/completed?field=priority&offset=20&limit=10",
            results = resultList
        )

        assertEquals(resultResponse, useCase.getPaginatedCompletedTasks(field, userId, offset, limit))
    }

    @Test
    fun `searchTasksByText returns paginated response with correct data`() {
        // Arrange
        val userId = 1
        val searchValue = "test"
        val offset = 0L
        val limit = 5
        val mockTasks = (1..5).map { Task(it, 1, userId, 1, "test task $it") }
        val totalCount = 10L

        every { repo.getTasksCountByText(userId, searchValue) } returns totalCount
        every { repo.searchTasksByText(searchValue, userId, offset, limit) } returns mockTasks

        // Act
        val result = useCase.searchTasksByText(searchValue, userId, offset, limit)

        assertEquals(totalCount, result.count)
        assertEquals(mockTasks, result.results)
        assertEquals("http://localhost:8080/tasks/search?value=test&offset=5&limit=5", result.nextPage)
        assertEquals(null, result.previousPage)

        verify(exactly = 1) { repo.getTasksCountByText(userId, searchValue) }
        verify(exactly = 1) { repo.searchTasksByText(searchValue, userId, offset, limit) }
    }

    @Test
    fun `searchTasksByText returns empty response when no tasks found`() {
        val userId = 1
        val searchValue = "nonexistent"
        val offset = 0L
        val limit = 5
        val totalCount = 0L

        every { repo.getTasksCountByText(userId, searchValue) } returns totalCount
        every { repo.searchTasksByText(searchValue, userId, offset, limit) } returns emptyList()

        val result = useCase.searchTasksByText(searchValue, userId, offset, limit)

        assertEquals(totalCount, result.count)
        assertEquals(emptyList(), result.results)
        assertEquals(null, result.nextPage)
        assertEquals(null, result.previousPage)
    }

    @Test
    fun `searchTasksByText returns correct previousPage link when not on first page`() {
        val userId = 1
        val searchValue = "test"
        val offset = 5L
        val limit = 5
        val mockTasks = (6..10).map { Task(it, 1, userId, 1, "test task $it") }
        val totalCount = 15L

        every { repo.getTasksCountByText(userId, searchValue) } returns totalCount
        every { repo.searchTasksByText(searchValue, userId, offset, limit) } returns mockTasks

        val result = useCase.searchTasksByText(searchValue, userId, offset, limit)

        assertEquals("http://localhost:8080/tasks/search?value=test&offset=0&limit=5", result.previousPage)
        assertEquals("http://localhost:8080/tasks/search?value=test&offset=10&limit=5", result.nextPage)
    }

    @Test
    fun `searchTasksByText returns null nextPage when on last page`() {
        val userId = 1
        val searchValue = "test"
        val offset = 5L
        val limit = 5
        val mockTasks = (6..8).map { Task(it, 1, userId, 1, "test task $it") }
        val totalCount = 8L

        every { repo.getTasksCountByText(userId, searchValue) } returns totalCount
        every { repo.searchTasksByText(searchValue, userId, offset, limit) } returns mockTasks

        val result = useCase.searchTasksByText(searchValue, userId, offset, limit)

        assertEquals(null, result.nextPage)
    }
}