package com.bortnik.todo.api.http.controllers

import com.bortnik.todo.domain.dto.user.UserCreate
import com.bortnik.todo.domain.dto.user.UserPublic
import com.bortnik.todo.domain.exceptions.user.UserAlreadyExists
import com.bortnik.todo.domain.exceptions.user.UserNotFound
import com.bortnik.todo.usecase.user.CreateUserUseCase
import com.bortnik.todo.usecase.user.DeleteUserUseCase
import com.bortnik.todo.usecase.user.GetUserUseCase
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val getUserUseCase: GetUserUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) {

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: Int): UserPublic {
        return getUserUseCase.getById(userId) ?: throw UserAlreadyExists("user with id $userId is not exist")
    }

    @GetMapping
    fun getUserByUsernameOrEmail(@RequestParam usernameOrEmail: String): UserPublic {
        return getUserUseCase.getByUsername(usernameOrEmail)
            ?: getUserUseCase.getByEmail(usernameOrEmail)
            ?: throw UserNotFound("User not found by '$usernameOrEmail'")
    }

    @PostMapping
    fun createUser(@RequestBody user: UserCreate): UserPublic {
        return createUserUseCase.save(user)
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(@RequestParam userId: Int) {
        deleteUserUseCase.deleteUser(userId)
    }
}