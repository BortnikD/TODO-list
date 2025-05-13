package com.bortnik.todo.api.http.openapi.controllers

import com.bortnik.todo.api.http.dto.UserUpdateRequest
import com.bortnik.todo.api.http.openapi.schemas.ApiErrorDoc
import com.bortnik.todo.domain.dto.user.UserPublic
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

@Tag(name = "Users", description = "API for managing user accounts")
@SecurityRequirement(name = "bearerAuth")
interface UserApiDocs {

    @Operation(
        summary = "Get the current user",
        description = "Returns the information of the authenticated user.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "User information retrieved successfully",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = UserPublic::class)
                )]
            )
        ]
    )
    fun getMe(@Parameter(hidden = true) user: UserDetails): UserPublic

    @Operation(
        summary = "Get a user by ID",
        description = "Returns user information by their unique ID.",
        parameters = [
            Parameter(
                name = "userId",
                `in` = ParameterIn.PATH,
                description = "ID of the user to retrieve",
                required = true,
                example = "123"
            )
        ],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "User information retrieved successfully",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = UserPublic::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Incorrect field",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            )
        ]
    )
    fun getUser(@PathVariable userId: Int): UserPublic

    @Operation(
        summary = "Get user by username or email",
        description = "Returns user information by their username or email.",
        parameters = [
            Parameter(
                name = "usernameOrEmail",
                `in` = ParameterIn.QUERY,
                description = "Username or email of the user to retrieve",
                required = true,
                example = "user@example.com"
            )
        ],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "User information retrieved successfully",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = UserPublic::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Incorrect field",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "User not found by the provided username or email",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            )
        ]
    )
    fun getUserByUsernameOrEmail(@RequestParam usernameOrEmail: String): UserPublic

    @Operation(
        summary = "Delete a user",
        description = "Delete user who called",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "User successfully deleted"
            ),
            ApiResponse(
                responseCode = "400",
                description = "Incorrect user field",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            )
        ]
    )
    fun deleteUser(@Parameter(hidden = true) user: UserDetails)

    @Operation(
        summary = "Update user information",
        description = "Updates the authenticated user's username or email. At least one field must be provided.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "User information retrieved successfully",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = UserPublic::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Validation error",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized - Bearer token missing or invalid",
                content = [Content(schema = Schema(implementation = ApiErrorDoc::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = [Content(schema = Schema(implementation = ApiErrorDoc::class))]
            )
        ]
    )
    fun updateUser(
        @RequestBody userUpdateRequest: UserUpdateRequest,
        @Parameter(hidden = true) user: UserDetails
    ): UserPublic
}
