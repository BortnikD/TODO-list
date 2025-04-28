package com.bortnik.todo.api.http

import com.bortnik.todo.domain.exceptions.CategoryNotFound
import com.bortnik.todo.domain.exceptions.InvalidRequestField
import com.bortnik.todo.domain.exceptions.TaskNotFound
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.resource.NoResourceFoundException

@ControllerAdvice
class ExceptionsHandler {

    @ExceptionHandler(CategoryNotFound::class)
    fun handleCategoryNotFound(ex: CategoryNotFound, request: WebRequest?): ResponseEntity<ApiError> {
        return buildResponseEntity(
            ApiError(
                error = "Category Not Found",
                message = ex.message ?: "Category not found",
                status = HttpStatus.NOT_FOUND,
                request = request!!
            )
        )
    }

    @ExceptionHandler(InvalidRequestField::class)
    fun handleInvalidRequestField(ex: InvalidRequestField, request: WebRequest?): ResponseEntity<ApiError> {
        return buildResponseEntity(
            ApiError(
                error = "Invalid Request Field",
                message = ex.message ?: "invalid request",
                status = HttpStatus.BAD_REQUEST,
                request = request!!
            )
        )
    }

    @ExceptionHandler(TaskNotFound::class)
    fun handleTaskNotFound(ex: TaskNotFound, request: WebRequest?): ResponseEntity<ApiError> {
        return buildResponseEntity(
            ApiError(
                error = "Task Not Found",
                message = ex.message ?: "task not found",
                status = HttpStatus.NOT_FOUND,
                request = request!!
            )
        )
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFoundException(ex: Exception, request: WebRequest?): ResponseEntity<ApiError> {
        return buildResponseEntity(
            ApiError(
                error = "No Resource Found",
                message = ex.message ?: "Not found",
                status = HttpStatus.NOT_FOUND,
                request = request!!
            )
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception, request: WebRequest?): ResponseEntity<ApiError> {
        return buildResponseEntity(
            ApiError(
                error = "Internal Server Error",
                message = ex.message ?: "An unexpected error occurred",
                status = HttpStatus.INTERNAL_SERVER_ERROR,
                path = request?.getDescription(false)?.replace("uri=", "") ?: "Unknown"
            )
        )
    }

    fun buildResponseEntity(apiError: ApiError): ResponseEntity<ApiError> {
        return ResponseEntity(apiError, apiError.status)
    }
}