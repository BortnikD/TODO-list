package com.bortnik.todo.domain.exceptions.auth

class AuthenticationException: Exception {
    constructor(message: String): super(message)
    constructor(message: String, cause: Throwable): super(message, cause)
}