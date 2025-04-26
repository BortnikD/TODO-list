package com.bortnik.todo.domain.entities

import java.time.LocalDateTime

data class Task (
    val id: Int,
    val categoryId: Int,
    val priority: Int = 1,
    val text: String,
    val createdAt: LocalDateTime? = null,
    val isCompleted: Boolean = false,
    val completedAt: LocalDateTime? = null
)
