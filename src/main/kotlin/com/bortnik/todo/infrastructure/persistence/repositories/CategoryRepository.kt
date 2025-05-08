package com.bortnik.todo.infrastructure.persistence.repositories

import com.bortnik.todo.domain.dto.CategoryCreate
import com.bortnik.todo.domain.entities.Category
import com.bortnik.todo.domain.exceptions.task.TaskNotFound
import com.bortnik.todo.domain.repositories.CategoryRepository
import com.bortnik.todo.infrastructure.persistence.entities.category.CategoryEntity
import com.bortnik.todo.infrastructure.persistence.entities.category.toDomain
import com.bortnik.todo.infrastructure.persistence.tables.CategoriesTable
import com.bortnik.todo.infrastructure.persistence.tables.UserTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class CategoryRepository: CategoryRepository {

    override fun addCategory(category: CategoryCreate): Category = transaction {
        CategoryEntity.new {
            userId = EntityID(category.userId, UserTable)
            name = category.name
        }.toDomain()
    }

    override fun getUserCategories(userId: Int): List<Category>? = transaction {
        CategoryEntity
            .find { CategoriesTable.userId eq userId }
            .toList()
            .map { it.toDomain() }
    }

    override fun getCategoryById(categoryId: Int): Category? = transaction {
        CategoryEntity.findById(categoryId)?.toDomain()
    }

    override fun deleteCategory(categoryId: Int) = transaction {
        CategoryEntity.findById(categoryId)?.delete() ?: throw TaskNotFound("Task not found")
    }
}