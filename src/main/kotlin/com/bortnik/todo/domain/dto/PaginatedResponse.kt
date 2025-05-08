package com.bortnik.todo.domain.dto

data class PaginatedResponse<T> (
    val count: Long = 0,
    val previousPage: String? = null,
    val nextPage: String? = null,
    val results: List<T>? = listOf()
)