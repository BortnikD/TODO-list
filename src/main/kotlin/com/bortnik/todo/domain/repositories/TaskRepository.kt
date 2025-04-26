package com.bortnik.todo.domain.repositories

import com.bortnik.todo.domain.dto.TaskCreate
import com.bortnik.todo.domain.dto.TaskUpdate
import com.bortnik.todo.domain.entities.Task

interface TaskRepository {

    /**
     * Add new task
     */
    fun addTask(task: TaskCreate): Task

    /**
     * Return sorted by field uncompleted tasks list, or unsorted list if field is null
     */
    fun getTasksSortedByFieldOrDefault(field: String): List<Task>

    /**
     * Return sorted by field completed tasks list, or unsorted list if field is null
     */
    fun getCompletedTasksSortedByFieldOrDefault(field: String): List<Task>

    /**
     * Update task by changed fields
     */
    fun updateTask(task: TaskUpdate): Task

    /**
     * Marked task as completed
     */
    fun completeTask(taskId: Int)
}