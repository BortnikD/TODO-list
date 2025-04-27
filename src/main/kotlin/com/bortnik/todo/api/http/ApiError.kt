package com.bortnik.todo.api.http

import org.springframework.http.HttpStatus
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

data class ApiError (
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val error: String,
    val message: String,
    val status: HttpStatus,
    val path: String
) {
    constructor(
        error: String,
        message: String,
        status: HttpStatus,
        request: WebRequest
    ) : this(
        error = error,
        message = message,
        status = status,
        path = request.getDescription(false).replace("uri=", "")
    )
}