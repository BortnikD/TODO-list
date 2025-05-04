package com.bortnik.todo.domain.dto.user

data class AuthResponse (
    val token: String,
    val username: String
)