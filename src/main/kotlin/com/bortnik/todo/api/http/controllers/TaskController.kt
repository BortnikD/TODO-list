package com.bortnik.todo.api.http.controllers

import com.bortnik.todo.domain.dto.TaskCreate
import com.bortnik.todo.domain.dto.TaskUpdate
import com.bortnik.todo.domain.entities.Task
import com.bortnik.todo.domain.exceptions.InvalidRequestField
import com.bortnik.todo.usecase.task.CreateTaskUseCase
import com.bortnik.todo.usecase.task.GetTaskUseCase
import com.bortnik.todo.usecase.task.UpdateTaskUseCase
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
    private val updateTaskUseCase: UpdateTaskUseCase
) {

    @GetMapping
    fun getUncompletedTasks(@RequestParam field: String = "priority"): List<Task> {
        return getTaskUseCase.getTasksSortedByFieldOrDefault(field)
    }

    @GetMapping("/completed")
    fun getCompletedTasks(@RequestParam field: String = "priority"): List<Task> {
        return getTaskUseCase.getCompletedTasksSortedByFieldOrDefault(field)
    }

    @PostMapping
    fun addTask(@RequestBody task: TaskCreate): Task {
        if (task.priority <= 0) throw InvalidRequestField("priority must be greet then 0")
        return createTaskUseCase.addTask(task)
    }

    @PatchMapping
    fun updateTask(@RequestBody task: TaskUpdate): Task {
        if (task.priority <= 0) throw InvalidRequestField("priority must be greet then 0")
        return updateTaskUseCase.updateTask(task)
    }

    @PatchMapping("/complete/{taskId}")
    fun completeTask(@PathVariable taskId: Int) {
        if (taskId <= 0) throw InvalidRequestField("task id must be greet then 0")
        return updateTaskUseCase.completeTask(taskId)
    }
}