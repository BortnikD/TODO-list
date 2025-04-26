package com.bortnik.todo.infrastructure.persistence.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object CategoriesTable: IntIdTable("categories") {
    val name = varchar("name", 32)
}