package com.bortnik.todo.domain.dto

data class CategoryCreate (
    val userId: Int,
    val name: String
)

data class CategoryUpdate (
    val id: Int,
    val userId: Int,
    val name: String
)