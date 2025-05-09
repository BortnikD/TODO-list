package com.bortnik.todo.usecase.user

import com.bortnik.todo.domain.dto.user.UserUpdate
import com.bortnik.todo.domain.entities.User
import com.bortnik.todo.domain.repositories.UserRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service

@Service
class UpdateUserUseCase(
    private val userRepository: UserRepository
) {

    @CacheEvict(value = ["user.byId", "user.byUsername", "user.byEmail"])
    fun update(user: UserUpdate): User {
        return userRepository.update(user)
    }
}