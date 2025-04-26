package com.bortnik.todo.domain.exceptions

class TaskNotFound: Exception {
    constructor(message: String): super(message)
    constructor(message: String, cause: Throwable): super(message, cause)
}