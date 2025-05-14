package com.bortnik.todo.api.http.validators.user

object UsernameValidator {
    private val usernameRegex = Regex("^[A-Za-z0-9_]+$")

    fun isValid(username: String): Boolean = usernameRegex.matches(username)
}