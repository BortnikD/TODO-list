package com.bortnik.todo.infrastructure.persistence.entities.user

import com.bortnik.todo.infrastructure.persistence.tables.UserTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<Int>): IntEntity(id) {

    companion object : IntEntityClass<UserEntity>(UserTable)

    var username by UserTable.username
    var email by UserTable.email
    val hashedPassword by UserTable.hashedPassword
    var createdAt by UserTable.createdAt
    var updatedAt by UserTable.updatedAt
}