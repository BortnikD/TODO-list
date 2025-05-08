package com.bortnik.todo.api.http.openapi.schemas

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Структура ответа при ошибке")
data class ApiErrorDoc(
    @Schema(description = "Краткое описание ошибки", example = "Error Name")
    val error: String,

    @Schema(description = "Сообщение об ошибке", example = "Error message")
    val message: String,

    @Schema(description = "HTTP статус", example = "Error status, for example NOT_FOUND")
    val status: String,

    @Schema(description = "Путь запроса", example = "URI for example /api/tasks")
    val path: String?
)