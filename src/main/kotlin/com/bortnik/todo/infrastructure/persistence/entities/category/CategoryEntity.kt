package com.bortnik.todo.infrastructure.persistence.entities.category

import com.bortnik.todo.infrastructure.persistence.tables.CategoriesTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CategoryEntity(id: EntityID<Int>): IntEntity(id) {

    companion object : IntEntityClass<CategoryEntity>(CategoriesTable)

    var userId by CategoriesTable.userId
    var name by CategoriesTable.name
}