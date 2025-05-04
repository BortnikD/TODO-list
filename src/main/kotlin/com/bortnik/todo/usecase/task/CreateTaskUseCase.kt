package com.bortnik.todo.usecase.task

import com.bortnik.todo.domain.dto.TaskCreate
import com.bortnik.todo.domain.entities.Task
import com.bortnik.todo.domain.exceptions.category.CategoryNotFound
import com.bortnik.todo.domain.repositories.TaskRepository
import com.bortnik.todo.usecase.category.GetCategoryUseCase
import org.springframework.stereotype.Service

@Service
class CreateTaskUseCase(
    private val taskRepository: TaskRepository,
    private val getCategoryUseCase: GetCategoryUseCase
) {

    fun addTask(task: TaskCreate): Task {
        getCategoryUseCase.getCategoryById(task.categoryId) ?: throw CategoryNotFound("Category not found")
        return taskRepository.addTask(task)
    }
}
