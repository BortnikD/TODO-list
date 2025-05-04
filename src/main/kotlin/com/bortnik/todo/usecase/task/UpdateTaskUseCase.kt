package com.bortnik.todo.usecase.task

import com.bortnik.todo.domain.dto.TaskUpdate
import com.bortnik.todo.domain.entities.Task
import com.bortnik.todo.domain.exceptions.category.CategoryNotFound
import com.bortnik.todo.domain.repositories.TaskRepository
import com.bortnik.todo.usecase.category.GetCategoryUseCase
import org.springframework.stereotype.Service

@Service
class UpdateTaskUseCase(
    private val taskRepository: TaskRepository,
    private val getCategoryUseCase: GetCategoryUseCase
) {

    fun updateTask(task: TaskUpdate): Task {
        getCategoryUseCase.getCategoryById(task.categoryId) ?: throw CategoryNotFound("Category not found")
        return taskRepository.updateTask(task)
    }

    fun completeTask(taskId: Int) {
        return taskRepository.completeTask(taskId)
    }
}