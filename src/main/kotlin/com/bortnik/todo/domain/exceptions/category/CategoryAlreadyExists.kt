package com.bortnik.todo.domain.exceptions.category

class CategoryAlreadyExists: Exception {
    constructor(message: String): super(message)
    constructor(message: String, cause: Throwable): super(message, cause)
}