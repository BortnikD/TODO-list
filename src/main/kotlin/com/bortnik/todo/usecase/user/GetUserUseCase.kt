package com.bortnik.todo.usecase.user

import com.bortnik.todo.domain.entities.User
import com.bortnik.todo.domain.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class GetUserUseCase(
    private val userRepository: UserRepository
) {

    fun getById(userId: Int): User? {
        return userRepository.getById(userId)
    }

    fun getByUsername(username: String): User? {
        return userRepository.getByUsername(username)
    }

    fun getByEmail(email: String): User? {
        return userRepository.getByEmail(email)
    }
}