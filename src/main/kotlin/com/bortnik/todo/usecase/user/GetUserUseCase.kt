package com.bortnik.todo.usecase.user

import com.bortnik.todo.domain.entities.User
import com.bortnik.todo.domain.repositories.UserRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class GetUserUseCase(
    private val userRepository: UserRepository
) {

    @Cacheable(value = ["user.byId"], key = "#userId")
    fun getById(userId: Int): User? {
        return userRepository.getById(userId)
    }

    @Cacheable(value = ["user.byUsername"], key = "#username")
    fun getByUsername(username: String): User? {
        return userRepository.getByUsername(username)
    }

    @Cacheable(value = ["user.byEmail"], key = "#email")
    fun getByEmail(email: String): User? {
        return userRepository.getByEmail(email)
    }
}