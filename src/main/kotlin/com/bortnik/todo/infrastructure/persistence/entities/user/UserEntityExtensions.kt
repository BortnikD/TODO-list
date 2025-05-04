package com.bortnik.todo.infrastructure.persistence.entities.user

import com.bortnik.todo.domain.entities.User

fun UserEntity.toDomain() =
    User(
        id = this.id.value,
        username = this.username,
        email = this.email,
        password = this.hashedPassword,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )