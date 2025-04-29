package com.bortnik.todo.domain.entities

data class User (
    val id: Int,
    val username: String,
    val email: String?,
    val password: String
)