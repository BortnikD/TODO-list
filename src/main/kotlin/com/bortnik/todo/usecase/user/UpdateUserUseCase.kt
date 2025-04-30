package com.bortnik.todo.usecase.user

import com.bortnik.todo.domain.dto.user.UserPublic
import com.bortnik.todo.domain.dto.user.UserUpdate
import com.bortnik.todo.domain.dto.user.toPublic
import com.bortnik.todo.domain.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UpdateUserUseCase(
    private val userRepository: UserRepository
) {

    fun update(user: UserUpdate): UserPublic {
        return userRepository.update(user).toPublic()
    }
}