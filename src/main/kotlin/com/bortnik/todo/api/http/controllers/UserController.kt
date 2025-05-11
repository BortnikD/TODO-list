package com.bortnik.todo.api.http.controllers

import com.bortnik.todo.api.http.exceptions.BadCredentials
import com.bortnik.todo.api.http.openapi.controllers.UserApiDocs
import com.bortnik.todo.domain.dto.user.UserPublic
import com.bortnik.todo.domain.dto.user.toPublic
import com.bortnik.todo.domain.exceptions.InvalidRequestField
import com.bortnik.todo.domain.exceptions.user.UserAlreadyExists
import com.bortnik.todo.domain.exceptions.user.UserNotFound
import com.bortnik.todo.infrastructure.security.user.getUserId
import com.bortnik.todo.usecase.user.DeleteUserUseCase
import com.bortnik.todo.usecase.user.GetUserUseCase
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val getUserUseCase: GetUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
): UserApiDocs {

    @GetMapping("/me")
    override fun getMe(@AuthenticationPrincipal user: UserDetails): UserPublic {
        return getUserUseCase.getByUsername(user.username)?.toPublic()
            ?: throw UserNotFound("User not found by username '${user.username}'")
    }

    @GetMapping("/{userId}")
    override fun getUser(@PathVariable userId: Int): UserPublic {
        validateUserId(userId)
        return getUserUseCase.getById(userId)?.toPublic()
            ?: throw UserAlreadyExists("user with id $userId is not exist")
    }

    @GetMapping
    override fun getUserByUsernameOrEmail(@RequestParam usernameOrEmail: String): UserPublic {
        if (usernameOrEmail.length < 3 || usernameOrEmail.length > 64) {
            throw BadCredentials("username of email is too long or short")
        }
        return getUserUseCase.getByUsername(usernameOrEmail)?.toPublic()
            ?: getUserUseCase.getByEmail(usernameOrEmail)?.toPublic()
            ?: throw UserNotFound("User not found by '$usernameOrEmail'")
    }

    @DeleteMapping("/deleteMyself")
    override fun deleteUser(@AuthenticationPrincipal user: UserDetails) {
        val userId = user.getUserId(getUserUseCase)
        validateUserId(userId)
        deleteUserUseCase.deleteUser(userId)
    }

    private fun validateUserId(userId: Int) {
        if (userId <= 0) {
            throw InvalidRequestField("user id must be greater than 0")
        }
    }
}