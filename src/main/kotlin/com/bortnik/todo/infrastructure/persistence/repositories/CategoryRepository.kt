package com.bortnik.todo.infrastructure.persistence.repositories

import com.bortnik.todo.domain.dto.CategoryCreate
import com.bortnik.todo.domain.entities.Category
import com.bortnik.todo.domain.exceptions.CategoryNotFound
import com.bortnik.todo.domain.exceptions.TaskNotFound
import com.bortnik.todo.domain.repositories.CategoryRepository
import com.bortnik.todo.infrastructure.persistence.entities.CategoryEntity
import com.bortnik.todo.infrastructure.persistence.entities.toDomain
import org.springframework.stereotype.Repository

@Repository
class CategoryRepository: CategoryRepository {

    override fun addCategory(category: CategoryCreate): Category {
        val entity = CategoryEntity.new {
            name = category.name
        }
        return entity.toDomain()
    }

    override fun getCategoryById(categoryId: Int): Category {
        val category = CategoryEntity.findById(categoryId) ?: throw CategoryNotFound("Category not found")
        return category.toDomain()
    }

    override fun deleteCategory(categoryId: Int) {
        val entity = CategoryEntity.findById(categoryId) ?: throw TaskNotFound("Task not found")
        entity.delete()
    }
}