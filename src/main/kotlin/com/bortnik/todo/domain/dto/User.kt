package com.bortnik.todo.domain.dto

data class UserCreate(
    val username: String,
    val email: String?,
    val password: String
)

data class UserPublic(
    val id: Int,
    val username: String,
    val email: String?
)