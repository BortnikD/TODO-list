package com.bortnik.todo.infrastructure.persistence.repositories

import com.bortnik.todo.domain.dto.user.UserCreate
import com.bortnik.todo.domain.dto.user.UserUpdate
import com.bortnik.todo.domain.entities.User
import com.bortnik.todo.domain.exceptions.user.UserNotFound
import com.bortnik.todo.domain.repositories.UserRepository
import com.bortnik.todo.infrastructure.persistence.entities.user.UserEntity
import com.bortnik.todo.infrastructure.persistence.entities.user.toDomain
import com.bortnik.todo.infrastructure.persistence.tables.UserTable
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class UserRepository: UserRepository {

    override fun save(user: UserCreate): User {
        return UserEntity.new {
            username = user.username
            email = user.email
            hashedPassword = user.password
        }.toDomain()
    }

    override fun getById(userId: Int): User? = transaction{
        UserEntity.findById(userId)?.toDomain()
    }

    override fun getByUsername(username: String): User? = transaction {
        UserEntity.find { UserTable.username eq username }.singleOrNull()?.toDomain()
    }

    override fun getByEmail(email: String): User? = transaction {
        UserEntity.find { UserTable.email eq email }.singleOrNull()?.toDomain()
    }

    override fun update(user: UserUpdate): User = transaction {
        UserEntity.findByIdAndUpdate(user.id) {
            it.username = user.username ?: it.username
            it.email = user.email ?: it.email
        }?.toDomain() ?: throw UserNotFound("Not found user to update")
    }

    override fun delete(userId: Int) = transaction {
        UserEntity.findById(userId)?.delete() ?: throw UserNotFound("Not found user to delete")
    }
}