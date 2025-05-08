package com.bortnik.todo.api.http.openapi.schemas

import com.bortnik.todo.domain.entities.Category
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Paginated response with a list of categories")
data class CategoryPaginatedResponse(
    @Schema(description = "Total number of categories", example = "42")
    val count: Long = 0,

    @Schema(description = "Link to the previous page", example = "/api/categories?offset=0&limit=10")
    val previousPage: String? = null,

    @Schema(description = "Link to the next page", example = "/api/categories?offset=20&limit=10")
    val nextPage: String? = null,

    @Schema(description = "List of categories")
    val results: List<Category>? = listOf()
)