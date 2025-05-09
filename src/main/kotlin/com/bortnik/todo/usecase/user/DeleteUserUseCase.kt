package com.bortnik.todo.usecase.user

import com.bortnik.todo.domain.repositories.UserRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service

@Service
class DeleteUserUseCase(
    private val userRepository: UserRepository
) {

    @CacheEvict(value = ["user.byId", "user.byUsername", "user.byEmail"])
    fun deleteUser(userId: Int) {
        userRepository.delete(userId)
    }
}