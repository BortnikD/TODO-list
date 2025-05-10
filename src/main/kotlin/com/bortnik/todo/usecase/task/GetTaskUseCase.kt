package com.bortnik.todo.usecase.task

import com.bortnik.todo.domain.dto.PaginatedResponse
import com.bortnik.todo.domain.entities.Task
import com.bortnik.todo.domain.repositories.TaskRepository
import com.bortnik.todo.usecase.generatePagesLinks
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable


@Service
class GetTaskUseCase(
    private val taskRepository: TaskRepository,
    @Value("\${base-url}") private val baseUrl: String
) {

    fun getTasksSortedByFieldOrDefault(
        field: String,
        userId: Int,
        offset: Long = 0,
        limit: Int = 10
    ): List<Task>? {
        return taskRepository.getTasksSortedByFieldOrDefault(field, offset, limit, userId)
    }

    fun getCompletedTasksSortedByFieldOrDefault(
        field: String,
        userId: Int,
        offset: Long = 0,
        limit: Int = 10
    ): List<Task>? {
        return taskRepository.getCompletedTasksSortedByFieldOrDefault(field, offset, limit, userId)
    }

    @Cacheable(value = ["tasks.uncompleted"], key = "#field + '_' + #userId + '_' + #offset + '_' + #limit")
    fun getPaginatedUncompletedTasks(
        field: String,
        userId: Int,
        offset: Long = 0,
        limit: Int = 10
    ): PaginatedResponse<Task> = transaction {
        val count = taskRepository.getTasksCountByUserId(userId)
        val basePath = "$baseUrl/tasks?field=${field}&"
        val pagesLinks = generatePagesLinks(offset, limit, count, basePath)
        val results = getTasksSortedByFieldOrDefault(field, userId, offset, limit)

        buildPaginatedResponse(count, pagesLinks, results)
    }

    @Cacheable(value = ["tasks.completed"], key = "#field + '_' + #userId + '_' + #offset + '_' + #limit")
    fun getPaginatedCompletedTasks(
        field: String,
        userId: Int,
        offset: Long = 0,
        limit: Int = 10
    ): PaginatedResponse<Task> = transaction {
        val count = taskRepository.getTasksCountByUserId(userId)
        val basePath = "$baseUrl/tasks/completed?field=$field&"
        val pagesLinks = generatePagesLinks(offset, limit, count, basePath)
        val results = getCompletedTasksSortedByFieldOrDefault(field, userId, offset, limit)

        buildPaginatedResponse(count, pagesLinks, results)
    }

    @Cacheable(value = ["tasks.search"], key = "#value + '_' + #userId + '_' + #offset + '_' + #limit")
    fun searchTasksByText(
        value: String,
        userId: Int,
        offset: Long = 0,
        limit: Int = 5
    ): PaginatedResponse<Task> = transaction {
        val count = taskRepository.getTasksCountByText(userId, value)
        val basePath = "$baseUrl/tasks/search?value=$value&"
        val pagesLinks = generatePagesLinks(offset, limit, count, basePath)
        val results = taskRepository.searchTasksByText(value, userId, offset, limit)

        buildPaginatedResponse(count, pagesLinks, results)
    }

    private fun buildPaginatedResponse(
        count: Long,
        pagesLinks: Pair<String?, String?>,
        results: List<Task>?
    ): PaginatedResponse<Task> {
        return PaginatedResponse(
            count = count,
            previousPage = pagesLinks.first,
            nextPage = pagesLinks.second,
            results = results
        )
    }
}
