package com.bortnik.todo.usecase.task

import com.bortnik.todo.domain.dto.TaskUpdate
import com.bortnik.todo.domain.entities.Task
import com.bortnik.todo.domain.exceptions.category.CategoryNotFound
import com.bortnik.todo.domain.exceptions.task.TaskNotFound
import com.bortnik.todo.domain.exceptions.user.AcceptError
import com.bortnik.todo.domain.repositories.TaskRepository
import com.bortnik.todo.usecase.category.GetCategoryUseCase
import org.springframework.stereotype.Service

@Service
class UpdateTaskUseCase(
    private val taskRepository: TaskRepository,
    private val getCategoryUseCase: GetCategoryUseCase
) {

    fun updateTask(task: TaskUpdate, userId: Int): Task {
        getCategoryUseCase.getCategoryById(task.categoryId)
            ?: throw CategoryNotFound("Category not found")

        validateTask(task.id, userId)
        return taskRepository.updateTask(task)
    }

    fun completeTask(taskId: Int, userId: Int) {
        validateTask(taskId, userId)
        return taskRepository.completeTask(taskId)
    }

    private fun validateTask(taskId: Int, userId: Int) {
        val task = taskRepository.getTaskById(taskId)
            ?: throw TaskNotFound("Task with id $taskId not found")

        if (task.userId != userId) {
            throw AcceptError("User $userId doesn't have permission to access task $taskId")
        }
    }
}