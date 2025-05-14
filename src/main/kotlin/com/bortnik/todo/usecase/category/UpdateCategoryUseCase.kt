package com.bortnik.todo.usecase.category

import com.bortnik.todo.domain.dto.CategoryUpdate
import com.bortnik.todo.domain.entities.Category
import com.bortnik.todo.domain.exceptions.category.CategoryNotFound
import com.bortnik.todo.domain.exceptions.auth.AcceptError
import com.bortnik.todo.domain.repositories.CategoryRepository
import org.springframework.stereotype.Service

@Service
class UpdateCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {

    fun update(categoryUpdate: CategoryUpdate): Category {
        val category = categoryRepository.getCategoryById(categoryUpdate.id)
            ?: throw CategoryNotFound("Category to update with id '${categoryUpdate.id}' not found")

        if (category.userId != categoryUpdate.userId) {
            throw AcceptError("You dont have access rights to update this category")
        }

        return categoryRepository.updateCategory(categoryUpdate)
    }
}