package com.bortnik.todo.domain.exceptions

class CategoryNotFound: Exception {
    constructor(message: String): super(message)
    constructor(message: String, cause: Throwable): super(message, cause)
}