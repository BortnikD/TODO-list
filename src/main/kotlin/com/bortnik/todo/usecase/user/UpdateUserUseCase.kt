package com.bortnik.todo.usecase.user

import com.bortnik.todo.domain.dto.user.UserUpdate
import com.bortnik.todo.domain.entities.User
import com.bortnik.todo.domain.exceptions.user.UserAlreadyExists
import com.bortnik.todo.domain.repositories.UserRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service

@Service
class UpdateUserUseCase(
    private val userRepository: UserRepository
) {

    @CacheEvict(value = ["user.byId", "user.byUsername", "user.byEmail"], allEntries = true)
    fun update(user: UserUpdate): User {
        user.username?.let { username ->
            if (userRepository.getByUsername(username) != null) {
                throw UserAlreadyExists("Sorry, '${username}' is already in use. " +
                        "Please pick another username.")
            }
        }
        user.email?.let { email ->
            if (userRepository.getByEmail(email) != null) {
                throw UserAlreadyExists("Email: '${email}' is already in use.")
            }
        }
        return userRepository.update(user)
    }
}