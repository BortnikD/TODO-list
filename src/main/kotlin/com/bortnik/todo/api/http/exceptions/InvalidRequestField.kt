package com.bortnik.todo.api.http.exceptions

class InvalidRequestField: Exception {
    constructor(message: String): super(message)
    constructor(message: String, cause: Throwable): super(message, cause)
}