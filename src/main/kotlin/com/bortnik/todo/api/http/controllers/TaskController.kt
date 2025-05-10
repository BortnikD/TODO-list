package com.bortnik.todo.api.http.controllers

import com.bortnik.todo.api.http.dto.TaskCreateRequest
import com.bortnik.todo.api.http.exceptions.BadCredentials
import com.bortnik.todo.api.http.openapi.controllers.TaskApiDocs
import com.bortnik.todo.domain.dto.PaginatedResponse
import com.bortnik.todo.domain.dto.TaskCreate
import com.bortnik.todo.domain.dto.TaskUpdate
import com.bortnik.todo.domain.entities.Task
import com.bortnik.todo.domain.exceptions.InvalidRequestField
import com.bortnik.todo.infrastructure.security.user.getUserId
import com.bortnik.todo.usecase.task.CreateTaskUseCase
import com.bortnik.todo.usecase.task.GetTaskUseCase
import com.bortnik.todo.usecase.task.UpdateTaskUseCase
import com.bortnik.todo.usecase.user.GetUserUseCase
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/tasks")
class TaskController(
    private val createTaskUseCase: CreateTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getUserUseCase: GetUserUseCase
): TaskApiDocs {

    @GetMapping
    override fun getUncompletedTasks(
        @RequestParam field: String,
        @RequestParam offset: Long?,
        @RequestParam limit: Int?,
        @AuthenticationPrincipal user: UserDetails
    ): PaginatedResponse<Task> {
        validatePagination(offset, limit)
        validateField(field)
        val userId = user.getUserId(getUserUseCase)

        return getTaskUseCase.getPaginatedUncompletedTasks(field, userId, offset ?: 0, limit ?: 10)
    }

    @GetMapping("/completed")
    override fun getCompletedTasks(
        @RequestParam field: String,
        @RequestParam offset: Long?,
        @RequestParam limit: Int?,
        @AuthenticationPrincipal user: UserDetails
    ): PaginatedResponse<Task> {
        validatePagination(offset, limit)
        validateField(field)
        val userId = user.getUserId(getUserUseCase)

        return getTaskUseCase.getPaginatedCompletedTasks(field, userId, offset ?: 0, limit ?: 10)
    }

    @PostMapping
    override fun addTask(
        @RequestBody task: TaskCreateRequest,
        @AuthenticationPrincipal user: UserDetails
    ): Task {
        validateTask(task.priority, task.categoryId, task.text)

        val userId = user.getUserId(getUserUseCase)

        val taskWithUserId = TaskCreate(
            userId = userId,
            categoryId = task.categoryId,
            priority = task.priority,
            text = task.text
        )
        return createTaskUseCase.addTask(taskWithUserId)
    }

    @PatchMapping
    override fun updateTask(
        @RequestBody task: TaskUpdate,
        @AuthenticationPrincipal user: UserDetails
    ): Task {
        validateTaskId(task.id)
        validateTask(task.priority, task.categoryId, task.text)
        val userId = user.getUserId(getUserUseCase)

        return updateTaskUseCase.updateTask(task, userId)
    }

    @PatchMapping("/complete/{taskId}")
    override fun completeTask(
        @PathVariable taskId: Int,
        @AuthenticationPrincipal user: UserDetails
    ) {
        validateTaskId(taskId)
        val userId = user.getUserId(getUserUseCase)

        return updateTaskUseCase.completeTask(taskId, userId)
    }

    private fun validatePagination(offset: Long?, limit: Int?) {
        offset?.let { value ->
            if (value < 0) {
                throw InvalidRequestField("offset value must by greater or equal to 0")
            }
        }
        limit?.let { value ->
            if (value < 0) {
                throw InvalidRequestField("offset value must by greater or equal to 0")
            }
        }
    }

    private fun validateField(field: String) {
        if (field !in arrayOf("created_at, priority, category")) {
            throw InvalidRequestField("search field must be in this list [created_at, priority, category]")
        }
    }

    private fun validateTask(priority: Int, categoryId: Int, text: String) {
        if (priority < 1) {
            throw BadCredentials("priority must be greater than 0")
        }
        else if (categoryId < 1) {
            throw BadCredentials("category id must be greater than 0")
        }
        else if (text.length < 3 || text.length > 256) {
            throw BadCredentials("text is too long or short")
        }
    }

    private fun validateTaskId(taskId: Int) {
        if (taskId < 1) {
            throw InvalidRequestField("task id must be greater than 0")
        }
    }
}