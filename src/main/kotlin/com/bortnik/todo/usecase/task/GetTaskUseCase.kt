package com.bortnik.todo.usecase.task

import com.bortnik.todo.domain.entities.Task
import com.bortnik.todo.domain.repositories.TaskRepository
import org.springframework.stereotype.Service

@Service
class GetTaskUseCase(
    private val taskRepository: TaskRepository
) {

    fun getTasksSortedByFieldOrDefault(field: String): List<Task> {
        return taskRepository.getTasksSortedByFieldOrDefault(field)
    }

    fun getCompletedTasksSortedByFieldOrDefault(field: String): List<Task> {
        return taskRepository.getCompletedTasksSortedByFieldOrDefault(field)
    }
}
