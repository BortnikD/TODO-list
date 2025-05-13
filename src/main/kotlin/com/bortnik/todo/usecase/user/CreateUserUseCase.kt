package com.bortnik.todo.usecase.user

import com.bortnik.todo.domain.dto.user.UserCreate
import com.bortnik.todo.domain.entities.User
import com.bortnik.todo.domain.exceptions.user.UserAlreadyExists
import com.bortnik.todo.domain.repositories.UserRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service

@Service
class CreateUserUseCase(
    private val userRepository: UserRepository
) {

    @CacheEvict(value = ["user.byId", "user.byUsername", "user.byEmail"], allEntries = true)
    fun save(user: UserCreate): User {
        validateUserUniqueness(user)
        return userRepository.save(user)
    }

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

}