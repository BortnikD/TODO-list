package com.bortnik.todo.infrastructure.persistence.repositories

import com.bortnik.todo.domain.dto.TaskCreate
import com.bortnik.todo.domain.dto.TaskUpdate
import com.bortnik.todo.domain.entities.Task
import com.bortnik.todo.domain.exceptions.InvalidRequestField
import com.bortnik.todo.domain.exceptions.TaskNotFound
import com.bortnik.todo.domain.repositories.TaskRepository
import com.bortnik.todo.infrastructure.persistence.entities.TaskEntity
import com.bortnik.todo.infrastructure.persistence.entities.toDomain
import com.bortnik.todo.infrastructure.persistence.tables.CategoriesTable
import com.bortnik.todo.infrastructure.persistence.tables.TasksTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SortOrder
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class TaskRepository: TaskRepository {

    private val sortableFields = mapOf(
        "created_at" to TasksTable.createdAt,
        "priority" to TasksTable.priority,
        "category" to TasksTable.categoryId
    )

    override fun addTask(task: TaskCreate): Task {
        val newTask = TaskEntity.new {
            categoryId = EntityID(task.categoryId, CategoriesTable)
            priority = task.priority
            text = task.text
        }

        return newTask.toDomain()
    }

    override fun getTasksSortedByFieldOrDefault(field: String): List<Task> {
        val column = sortableFields[field] ?: throw InvalidRequestField("Unsupported field: $field")

        return TaskEntity.all()
            .orderBy(column to SortOrder.ASC)
            .filter { !it.isCompleted }
            .map { it.toDomain() }
    }

    override fun getCompletedTasksSortedByFieldOrDefault(field: String): List<Task> {
        val column = sortableFields[field] ?: throw InvalidRequestField("Unsupported field: $field")

        return TaskEntity.all()
            .orderBy(column to SortOrder.ASC)
            .filter { it.isCompleted }
            .map { it.toDomain() }
    }

    override fun updateTask(task: TaskUpdate): Task {
        val updatedTask = TaskEntity.findByIdAndUpdate(task.id) {
            it.categoryId = EntityID(task.categoryId, CategoriesTable)
            it.priority = task.priority
            it.text = task.text
        } ?: throw TaskNotFound("task to update not found")

        return updatedTask.toDomain()
    }

    override fun completeTask(taskId: Int) {
        TaskEntity.findByIdAndUpdate(taskId) {
            it.isCompleted = true
            it.completedAt = LocalDateTime.now()
        } ?: throw TaskNotFound("completed task not found")
    }
}
