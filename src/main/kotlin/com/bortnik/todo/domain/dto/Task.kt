package com.bortnik.todo.domain.dto

data class TaskCreate(
    val userId: Int,
    val categoryId: Int,
    val priority: Int = 1,
    val text: String,
)

data class TaskUpdate(
    val id: Int,
    val categoryId: Int,
    val priority: Int,
    val text: String
)

data class TaskComplete(
    val id: Int,
    val userId: Int
)