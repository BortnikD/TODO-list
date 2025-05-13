package com.bortnik.todo.api.http.dto

data class CategoryCreateRequest (
    val name: String
)

data class CategoryUpdateRequest (
    val id: Int,
    val name: String
)