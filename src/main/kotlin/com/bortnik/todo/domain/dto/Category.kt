package com.bortnik.todo.domain.dto

data class CategoryCreateRequest (
    val name: String
)

data class CategoryCreate (
    val userId: Int,
    val name: String
)