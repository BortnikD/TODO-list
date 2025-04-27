package com.bortnik.todo.infrastructure.persistence.entities.task

import com.bortnik.todo.infrastructure.persistence.tables.TasksTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TaskEntity(id: EntityID<Int>): IntEntity(id) {

    companion object : IntEntityClass<TaskEntity>(TasksTable)

    var categoryId by TasksTable.categoryId
    var priority by TasksTable.priority
    var text by TasksTable.text
    var createdAt by TasksTable.createdAt
    var isCompleted by TasksTable.isCompleted
    var completedAt by TasksTable.completedAt
}