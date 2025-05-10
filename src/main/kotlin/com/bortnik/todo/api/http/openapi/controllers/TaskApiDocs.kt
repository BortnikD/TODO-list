package com.bortnik.todo.api.http.openapi.controllers

import com.bortnik.todo.api.http.dto.TaskCreateRequest
import com.bortnik.todo.api.http.openapi.schemas.ApiErrorDoc
import com.bortnik.todo.api.http.openapi.schemas.TaskPaginatedResponse
import com.bortnik.todo.domain.dto.PaginatedResponse
import com.bortnik.todo.domain.dto.TaskUpdate
import com.bortnik.todo.domain.entities.Task
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@Tag(name = "Tasks", description = "API for managing tasks")
@SecurityRequirement(name = "bearerAuth")
interface TaskApiDocs {

    @Operation(
        summary = "Get uncompleted tasks",
        description = "Returns a list of completed tasks sorted by the specified field, " +
                "field examples = created_at, priority, category",
        parameters = [
            Parameter(
                name = "field",
                `in` = ParameterIn.QUERY,
                description = "Field to sort by (default is 'priority')",
                example = "priority"
            )
        ],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "List of tasks",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = TaskPaginatedResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid query parameter",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Tasks not found",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            )
        ]
    )
    fun getUncompletedTasks(
        @RequestParam field: String = "priority",
        @RequestParam offset: Long?,
        @RequestParam limit: Int?,
        @Parameter(hidden = true) user: UserDetails
    ): PaginatedResponse<Task>

    @Operation(
        summary = "Get completed tasks",
        description = "Returns a list of completed tasks sorted by the specified field, " +
                "field examples = created_at, priority, category",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "List of tasks",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = TaskPaginatedResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid query parameter",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Tasks not found",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            )
        ]
    )
    fun getCompletedTasks(
        @RequestParam field: String = "priority",
        @RequestParam offset: Long?,
        @RequestParam limit: Int?,
        @Parameter(hidden = true) user: UserDetails,
    ): PaginatedResponse<Task>

    @Operation(
        summary = "Add a task",
        description = "Creates a new task for the current user",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Created task",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = Task::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request parameter",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "409",
                description = "User already exists",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            )
        ]
    )
    fun addTask(
        @RequestBody task: TaskCreateRequest,
        @Parameter(hidden = true) user: UserDetails
    ): Task

    @Operation(
        summary = "Update a task",
        description = "Updates an existing task for the current user",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Updated task",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = Task::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request parameter",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Category or Task not found",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Access denied",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            )
        ]
    )
    fun updateTask(
        @RequestBody task: TaskUpdate,
        @Parameter(hidden = true) user: UserDetails
    ): Task

    @Operation(
        summary = "Complete a task",
        description = "Marks the task as completed",
        responses = [
            ApiResponse(
                responseCode = "400",
                description = "Invalid request parameter",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Category or Task not found",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Access denied",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            )
        ]
    )
    fun completeTask(
        @Parameter(description = "Task ID") taskId: Int,
        @Parameter(hidden = true) user: UserDetails
    )

    @Operation(
        summary = "Search tasks by text",
        description = "Searches for tasks containing the specified text. Returns a list of tasks that match the search criteria.",
        parameters = [
            Parameter(
                name = "value",
                `in` = ParameterIn.QUERY,
                description = "Text to search for in task titles or descriptions.",
                required = true,
                example = "meeting"
            )
        ],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "List of tasks matching the search text",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = TaskPaginatedResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request parameter",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "No tasks found matching the search text",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            )
        ]
    )
    fun searchTasksByText(
        @RequestParam value: String,
        @RequestParam offset: Long?,
        @RequestParam limit: Int?,
        @Parameter(hidden = true) user: UserDetails,
    ): PaginatedResponse<Task>
}
