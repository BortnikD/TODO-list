package com.bortnik.todo.infrastructure.security.user

import com.bortnik.todo.domain.exceptions.user.UserNotFound
import com.bortnik.todo.domain.repositories.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.getByUsername(username)
            ?: throw UserNotFound("User not found with username: $username")

        return User.builder()
            .username(user.username)
            .password(user.password)
            .authorities(SimpleGrantedAuthority("USER"))
            .build()
    }
}