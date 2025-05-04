package com.bortnik.todo.infrastructure.persistence.repositories

import com.bortnik.todo.domain.dto.TaskCreate
import com.bortnik.todo.domain.dto.TaskUpdate
import com.bortnik.todo.domain.entities.Task
import com.bortnik.todo.domain.exceptions.InvalidRequestField
import com.bortnik.todo.domain.exceptions.task.TaskNotFound
import com.bortnik.todo.domain.repositories.TaskRepository
import com.bortnik.todo.infrastructure.persistence.entities.task.TaskEntity
import com.bortnik.todo.infrastructure.persistence.entities.task.toDomain
import com.bortnik.todo.infrastructure.persistence.tables.CategoriesTable
import com.bortnik.todo.infrastructure.persistence.tables.TasksTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class TaskRepository: TaskRepository {

    private val sortableFields = mapOf(
        "created_at" to TasksTable.createdAt,
        "priority" to TasksTable.priority,
        "category" to TasksTable.categoryId
    )

    override fun addTask(task: TaskCreate): Task = transaction {
        val newTask = TaskEntity.new {
            categoryId = EntityID(task.categoryId, CategoriesTable)
            priority = task.priority
            text = task.text
        }
        newTask.toDomain()
    }

    override fun getTasksSortedByFieldOrDefault(field: String): List<Task> = transaction {
        val column = sortableFields[field] ?: throw InvalidRequestField("Unsupported field: $field")
        TaskEntity.all()
            .orderBy(column to SortOrder.ASC)
            .filter { !it.isCompleted }
            .map { it.toDomain() }
    }

    override fun getCompletedTasksSortedByFieldOrDefault(field: String): List<Task> = transaction {
        val column = sortableFields[field] ?: throw InvalidRequestField("Unsupported field: $field")
        TaskEntity.all()
            .orderBy(column to SortOrder.ASC)
            .filter { it.isCompleted }
            .map { it.toDomain() }
    }

    override fun updateTask(task: TaskUpdate): Task = transaction {
        val updatedTask = TaskEntity.findByIdAndUpdate(task.id) {
            it.categoryId = EntityID(task.categoryId, CategoriesTable)
            it.priority = task.priority
            it.text = task.text
        } ?: throw TaskNotFound("task to update not found")
        updatedTask.toDomain()
    }

    override fun completeTask(taskId: Int) {
        transaction {
            TaskEntity.findByIdAndUpdate(taskId) {
                it.isCompleted = true
                it.completedAt = LocalDateTime.now()
            } ?: throw TaskNotFound("completed task not found")
        }
    }
}
