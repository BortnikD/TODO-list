package com.bortnik.todo.domain.dto.user

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

data class UserUpdate(
    val id: Int,
    val username: String?,
    val email: String?
)