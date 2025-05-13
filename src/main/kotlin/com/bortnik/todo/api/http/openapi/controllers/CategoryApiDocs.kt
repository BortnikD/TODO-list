package com.bortnik.todo.api.http.openapi.controllers

import com.bortnik.todo.api.http.dto.CategoryCreateRequest
import com.bortnik.todo.api.http.dto.CategoryUpdateRequest
import com.bortnik.todo.api.http.openapi.schemas.ApiErrorDoc
import com.bortnik.todo.api.http.openapi.schemas.CategoryPaginatedResponse
import com.bortnik.todo.domain.dto.PaginatedResponse
import com.bortnik.todo.domain.entities.Category
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@Tag(name = "Categories", description = "API for managing categories")
@SecurityRequirement(name = "bearerAuth")
interface CategoryApiDocs {

    @Operation(
        summary = "Add a new category",
        description = "Creates a new category for the authenticated user",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Category successfully created",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = Category::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request body",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            )
        ]
    )
    fun addCategory(
        @RequestBody category: CategoryCreateRequest,
        @Parameter(hidden = true) user: UserDetails
    ): Category

    @Operation(
        summary = "Delete a category",
        description = "Deletes the specified category for the authenticated user",
        parameters = [
            Parameter(
                name = "categoryId",
                `in` = ParameterIn.PATH,
                description = "The ID of the category to delete",
                required = true,
                example = "1"
            )
        ],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Category successfully deleted"
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid category ID",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "403",
                description = "You dont have access rights",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Category not found",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            )
        ]
    )
    fun deleteCategory(
        @PathVariable categoryId: Int,
        @Parameter(hidden = true) user: UserDetails
    )

    @Operation(
        summary = "Get categories for the authenticated user",
        description = "Fetches a paginated list of categories for the authenticated user",
        parameters = [
            Parameter(
                name = "offset",
                `in` = ParameterIn.QUERY,
                description = "The offset for pagination",
                example = "0"
            ),
            Parameter(
                name = "limit",
                `in` = ParameterIn.QUERY,
                description = "The number of items per page",
                example = "10"
            )
        ],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "List of categories",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = CategoryPaginatedResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid query parameters",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "No categories found",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            )
        ]
    )
    fun getUserCategories(
        @RequestParam offset: Long?,
        @RequestParam limit: Int?,
        @Parameter(hidden = true) user: UserDetails
    ): PaginatedResponse<Category>

    @Operation(
        summary = "Update a category",
        description = "Update category for the authenticated user",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Category successfully updated",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = Category::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request body",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Accept error",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Category not found",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            )
        ]
    )
    fun updateUserCategories(
        @RequestBody categoryUpdateRequest: CategoryUpdateRequest,
        @Parameter(hidden = true) user: UserDetails
    ): Category
}
