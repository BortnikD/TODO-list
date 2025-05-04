package com.bortnik.todo.usecase.user

import com.bortnik.todo.domain.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class DeleteUserUseCase(
    private val userRepository: UserRepository
) {

    fun deleteUser(userId: Int) {
        userRepository.delete(userId)
    }
}