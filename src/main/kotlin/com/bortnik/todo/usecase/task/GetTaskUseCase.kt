package com.bortnik.todo.usecase.task

import com.bortnik.todo.domain.dto.PaginatedResponse
import com.bortnik.todo.domain.entities.Task
import com.bortnik.todo.domain.repositories.TaskRepository
import com.bortnik.todo.usecase.generatePagesLinks
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable


@Service
class GetTaskUseCase(
    private val taskRepository: TaskRepository,
    @Value("\${base-url}") private val baseUrl: String
) {

    @Cacheable(value = ["tasks.uncompleted"], key = "#field + '_' + #userId + '_' + #offset + '_' + #limit")
    fun getPaginatedUncompletedTasks(
        field: String,
        userId: Int,
        offset: Long = 0,
        limit: Int = 10
    ): PaginatedResponse<Task> {
        val count = taskRepository.getTasksCountByUserId(userId, false)
        val path = "?field=${field}&"
        val results = taskRepository.getTasksSortedByFieldOrDefault(field, offset, limit, userId)
        return buildPaginatedResponse(
            count = count,
            offset = offset,
            limit = limit,
            results = results,
            path = path
        )
    }

    @Cacheable(value = ["tasks.completed"], key = "#field + '_' + #userId + '_' + #offset + '_' + #limit")
    fun getPaginatedCompletedTasks(
        field: String,
        userId: Int,
        offset: Long = 0,
        limit: Int = 10
    ): PaginatedResponse<Task> {
        val count = taskRepository.getTasksCountByUserId(userId, true)
        val path = "/completed?field=$field&"
        val results = taskRepository.getCompletedTasksSortedByFieldOrDefault(field, offset, limit, userId)
        return buildPaginatedResponse(
            count = count,
            offset = offset,
            limit = limit,
            results = results,
            path = path
        )
    }

    @Cacheable(value = ["tasks.search"], key = "#value + '_' + #userId + '_' + #offset + '_' + #limit")
    fun searchTasksByText(
        value: String,
        userId: Int,
        offset: Long = 0,
        limit: Int = 5
    ): PaginatedResponse<Task> {
        val count = taskRepository.getTasksCountByText(userId, value)
        val path = "/search?value=$value&"
        val results = taskRepository.searchTasksByText(value, userId, offset, limit)
        return buildPaginatedResponse(
            count = count,
            offset = offset,
            limit = limit,
            results = results,
            path = path
        )
    }

    private fun buildPaginatedResponse(
        count: Long,
        offset: Long,
        limit: Int,
        results: List<Task>?,
        path: String
    ): PaginatedResponse<Task> {
        val basePath = "$baseUrl/tasks${path}"
        val pagesLinks = generatePagesLinks(offset, limit, count, basePath)
        return PaginatedResponse(
            count = count,
            previousPage = pagesLinks.first,
            nextPage = pagesLinks.second,
            results = results
        )
    }
}
