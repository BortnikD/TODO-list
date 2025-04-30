package com.bortnik.todo.domain.repositories

import com.bortnik.todo.domain.dto.UserCreate
import com.bortnik.todo.domain.dto.UserPublic

interface UserRepository {

    /**
     * Create new user
     */
    fun save(user: UserCreate): UserPublic

    /**
     * get user by user id
     */
    fun getById(userId: Int): UserPublic

    /**
     * get user by username
     */
    fun getByUsername(username: String): UserPublic

    /**
     * get user by email
     */
    fun getByEmail(email: String): UserPublic

    /**
     * delete user by user id
     */
    fun delete(userId: Int)
}