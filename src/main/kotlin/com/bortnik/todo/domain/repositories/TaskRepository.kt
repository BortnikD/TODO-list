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
     * get tasks count by user id
     */
    fun getTasksCountByUserId(userId: Int): Long

    /**
     * get tasks count by user id and text to search
     */
    fun getTasksCountByText(userId: Int, text: String): Long

    /**
     * get task by id
     */
    fun getTaskById(taskId: Int): Task?

    /**
     * Return sorted by field uncompleted tasks list, or unsorted list if field is null
     */
    fun getTasksSortedByFieldOrDefault(field: String, offset: Long, limit: Int, userId: Int): List<Task>?

    /**
     * Return sorted by field completed tasks list, or unsorted list if field is null
     */
    fun getCompletedTasksSortedByFieldOrDefault(field: String, offset: Long, limit: Int, userId: Int): List<Task>?

    /**
     * search tasks by substring of task text
     */
    fun searchTasksByText(value: String, userId: Int, offset: Long, limit: Int): List<Task>?

    /**
     * Update task by changed fields
     */
    fun updateTask(task: TaskUpdate): Task

    /**
     * Marked task as completed
     */
    fun completeTask(taskId: Int)
}