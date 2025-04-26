package com.bortnik.todo.infrastructure.persistence.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.CurrentDateTime

object TasksTable : IntIdTable("tasks") {
    val categoryId = reference("category_id", CategoriesTable, ReferenceOption.CASCADE)
    val priority = integer("priority")
    val text = varchar("text", 256)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val isCompleted = bool("is_completed").default(false)
    val completedAt = datetime("completed_at").nullable()
}