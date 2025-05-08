package com.bortnik.todo.api.http.exceptions

class BadCredentials: Exception {
    constructor(message: String): super(message)
    constructor(message: String, cause: Throwable): super(message, cause)
}