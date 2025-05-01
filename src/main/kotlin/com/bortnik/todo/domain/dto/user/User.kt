package com.bortnik.todo.domain.dto.user

import java.time.LocalDateTime

data class UserCreate(
    val username: String,
    val email: String?,
    val password: String
)

data class UserPublic(
    val id: Int,
    val username: String,
    val email: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class UserUpdate(
    val id: Int,
    val username: String?,
    val email: String?
)