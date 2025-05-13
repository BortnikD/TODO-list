package com.bortnik.todo.infrastructure.security.user

import com.bortnik.todo.domain.entities.User
import com.bortnik.todo.domain.exceptions.user.UserNotFound
import com.bortnik.todo.usecase.user.GetUserUseCase
import org.springframework.security.core.userdetails.UserDetails

fun UserDetails.getUserId(getUserUseCase: GetUserUseCase): Int {
    return getUserUseCase.getByUsername(this.username)?.id
        ?: throw UserNotFound("Not found user with username '${this.username}'")
}

fun UserDetails.getDomainUser(getUserUseCase: GetUserUseCase): User {
    return getUserUseCase.getByUsername(this.username)
        ?: throw UserNotFound("Not found user with username '${this.username}'")
}