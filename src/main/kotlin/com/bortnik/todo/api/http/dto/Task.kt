package com.bortnik.todo.api.http.dto

data class TaskCreateRequest (
    val categoryId: Int,
    val priority: Int = 1,
    val text: String,
)