package com.bortnik.todo.api.http.openapi.controllers

import com.bortnik.todo.api.http.openapi.schemas.ApiErrorDoc
import com.bortnik.todo.domain.dto.user.AuthResponse
import com.bortnik.todo.domain.dto.user.UserCreate
import com.bortnik.todo.domain.dto.user.UserLogin
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "Authentication", description = "API for user authentication and registration")
interface AuthApiDocs {

    @Operation(
        summary = "Register a new user",
        description = "Registers a new user by providing a username, email, and password. " +
                "Validations are applied to ensure the username, email, and password meet the requirements.",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User registration details",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = UserCreate::class)
            )]
        ),
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "User successfully registered",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = AuthResponse::class)
                )]
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
                responseCode = "409",
                description = "User Already Exists",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            )
        ]
    )
    fun register(@RequestBody user: UserCreate): AuthResponse

    @Operation(
        summary = "Authenticate a user",
        description = "Authenticates a user by providing a username and password. " +
                "Returns an authentication token if the login is successful.",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User login credentials",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = UserLogin::class)
            )]
        ),
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Authentication successful",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = AuthResponse::class)
                )]
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
                description = "Bad Credentials Exception",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "User Not Found",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ApiErrorDoc::class)
                    )
                ]
            )
        ]
    )
    fun authenticate(@RequestBody user: UserLogin): AuthResponse
}
