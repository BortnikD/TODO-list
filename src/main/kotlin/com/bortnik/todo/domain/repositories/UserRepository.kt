package com.bortnik.todo.domain.repositories

import com.bortnik.todo.domain.dto.UserCreate
import com.bortnik.todo.domain.entities.User

interface UserRepository {

    /**
     * Create new user
     */
    fun save(user: UserCreate): User

    /**
     * get user by user id
     */
    fun getById(userId: Int): User?

    /**
     * get user by username
     */
    fun getByUsername(username: String): User?

    /**
     * get user by email
     */
    fun getByEmail(email: String): User?

    /**
     * delete user by user id
     */
    fun delete(userId: Int)
}