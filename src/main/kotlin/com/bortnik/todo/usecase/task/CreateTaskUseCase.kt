package com.bortnik.todo.usecase.task

import com.bortnik.todo.domain.dto.TaskCreate
import com.bortnik.todo.domain.entities.Task
import com.bortnik.todo.domain.exceptions.category.CategoryNotFound
import com.bortnik.todo.domain.exceptions.user.AcceptError
import com.bortnik.todo.domain.repositories.TaskRepository
import com.bortnik.todo.usecase.category.GetCategoryUseCase
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service

@Service
class CreateTaskUseCase(
    private val taskRepository: TaskRepository,
    private val getCategoryUseCase: GetCategoryUseCase
) {

    @CacheEvict(value = ["tasks.uncompleted", "tasks.search"], allEntries = true)
    fun addTask(task: TaskCreate): Task {
        val category = getCategoryUseCase.getCategoryById(task.categoryId)
            ?: throw CategoryNotFound("Category not found")
        if (task.userId != category.userId) {
            throw AcceptError("This category belongs to another user")
        }
        return taskRepository.addTask(task)
    }
}
