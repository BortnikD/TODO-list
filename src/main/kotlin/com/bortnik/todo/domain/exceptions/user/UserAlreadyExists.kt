package com.bortnik.todo.domain.exceptions.user

class UserAlreadyExists: Exception {
    constructor(message: String): super(message)
    constructor(message: String, cause: Throwable): super(message, cause)
}