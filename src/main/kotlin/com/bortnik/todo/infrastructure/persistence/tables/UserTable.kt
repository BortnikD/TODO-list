package com.bortnik.todo.infrastructure.persistence.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object UserTable: IntIdTable("users") {
    val username = varchar("username", 64)
    val email = varchar("email", 64).nullable()
    val hashedPassword = varchar("hashed_password", 256)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
}