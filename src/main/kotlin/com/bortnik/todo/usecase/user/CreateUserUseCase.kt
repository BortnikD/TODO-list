package com.bortnik.todo.usecase.user

import com.bortnik.todo.domain.dto.user.UserCreate
import com.bortnik.todo.domain.dto.user.UserPublic
import com.bortnik.todo.domain.dto.user.toPublic
import com.bortnik.todo.domain.exceptions.user.UserAlreadyExists
import com.bortnik.todo.domain.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class CreateUserUseCase(
    private val userRepository: UserRepository
) {

    private fun validateUserUniqueness(user: UserCreate) {
        if (userRepository.getByUsername(user.username) != null) {
            throw UserAlreadyExists("User with username '${user.username}' already exists")
        }
        user.email?.let { email ->
            if (userRepository.getByEmail(email) != null) {
                throw UserAlreadyExists("User with email '$email' already exists")
            }
        }
    }

    fun save(user: UserCreate): UserPublic {
        validateUserUniqueness(user)
        return userRepository.save(user).toPublic()
    }
}