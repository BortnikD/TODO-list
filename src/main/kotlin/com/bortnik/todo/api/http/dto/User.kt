package com.bortnik.todo.api.http.dto

data class UserUpdateRequest(
    val username: String?,
    val email: String?
)