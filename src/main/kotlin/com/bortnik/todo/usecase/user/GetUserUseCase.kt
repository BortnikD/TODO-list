package com.bortnik.todo.usecase.user

import com.bortnik.todo.domain.dto.user.UserPublic
import com.bortnik.todo.domain.dto.user.toPublic
import com.bortnik.todo.domain.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class GetUserUseCase(
    private val userRepository: UserRepository
) {

    fun getById(userId: Int): UserPublic? {
        return userRepository.getById(userId)?.toPublic()
    }

    fun getByUsername(username: String): UserPublic? {
        return userRepository.getByUsername(username)?.toPublic()
    }

    fun getByEmail(email: String): UserPublic? {
        return userRepository.getByEmail(email)?.toPublic()
    }
}