package com.bortnik.todo.usecase.task

import com.bortnik.todo.domain.dto.PaginatedResponse
import com.bortnik.todo.domain.entities.Task
import com.bortnik.todo.domain.repositories.TaskRepository
import com.bortnik.todo.usecase.generatePagesLinks
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Value


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

    fun getPaginatedUncompletedTasks(
        field: String,
        userId: Int,
        offset: Long = 0,
        limit: Int = 10
    ): PaginatedResponse<Task> = transaction {
        val count = taskRepository.getCount(field, userId)
        val basePath = "$baseUrl/tasks?field=$field"
        val pagesLinks = generatePagesLinks(offset, limit, count, basePath)

        PaginatedResponse<Task>(
            count = count,
            previousPage = pagesLinks.first,
            nextPage = pagesLinks.second,
            results = getTasksSortedByFieldOrDefault(field, userId, offset, limit)
        )
    }

    fun getPaginatedCompletedTasks(
        field: String,
        userId: Int,
        offset: Long = 0,
        limit: Int = 10
    ): PaginatedResponse<Task> = transaction {
        val count = taskRepository.getCount(field, userId)
        val basePath = "$baseUrl/tasks/completed?field=$field"
        val pagesLinks = generatePagesLinks(offset, limit, count, basePath)

        PaginatedResponse<Task>(
            count = count,
            previousPage = pagesLinks.first,
            nextPage = pagesLinks.second,
            results = getCompletedTasksSortedByFieldOrDefault(field, userId, offset, limit)
        )
    }
}
