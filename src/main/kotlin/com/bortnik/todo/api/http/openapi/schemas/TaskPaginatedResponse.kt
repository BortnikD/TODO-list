package com.bortnik.todo.api.http.openapi.schemas

import com.bortnik.todo.domain.entities.Task
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Paginated response with a list of tasks")
data class TaskPaginatedResponse(
    @Schema(description = "Total number of tasks", example = "42")
    val count: Long = 0,

    @Schema(description = "Link to the previous page", example = "/api/tasks?offset=0&limit=10")
    val previousPage: String? = null,

    @Schema(description = "Link to the next page", example = "/api/tasks?offset=20&limit=10")
    val nextPage: String? = null,

    @Schema(description = "List of tasks")
    val results: List<Task>? = listOf()
)