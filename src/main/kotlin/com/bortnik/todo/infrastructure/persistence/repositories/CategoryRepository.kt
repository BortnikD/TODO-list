package com.bortnik.todo.infrastructure.persistence.repositories

import com.bortnik.todo.domain.dto.CategoryCreate
import com.bortnik.todo.domain.entities.Category
import com.bortnik.todo.domain.exceptions.TaskNotFound
import com.bortnik.todo.domain.repositories.CategoryRepository
import com.bortnik.todo.infrastructure.persistence.entities.category.CategoryEntity
import com.bortnik.todo.infrastructure.persistence.entities.category.toDomain
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class CategoryRepository: CategoryRepository {

    override fun addCategory(category: CategoryCreate): Category = transaction {
        val entity = CategoryEntity.new {
            name = category.name
        }
        entity.toDomain()
    }

    override fun getCategoryById(categoryId: Int): Category? = transaction {
        val category = CategoryEntity.findById(categoryId)
        category?.toDomain()
    }

    override fun deleteCategory(categoryId: Int) = transaction {
        val entity = CategoryEntity.findById(categoryId) ?: throw TaskNotFound("Task not found")
        entity.delete()
    }
}