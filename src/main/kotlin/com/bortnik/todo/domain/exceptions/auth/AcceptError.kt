package com.bortnik.todo.domain.exceptions.auth

class AcceptError: Exception {
    constructor(message: String): super(message)
    constructor(message: String, cause: Throwable): super(message, cause)
}