package com.bortnik.todo.usecase.category

import com.bortnik.todo.domain.exceptions.category.CategoryNotFound
import com.bortnik.todo.domain.exceptions.user.AcceptError
import com.bortnik.todo.domain.repositories.CategoryRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service

@Service
class DeleteCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {

    @CacheEvict(value = ["category.byId", "categories.byUserId"])
    fun deleteCategory(categoryId: Int, userId: Int) {
        val category = categoryRepository.getCategoryById(categoryId)
            ?: throw CategoryNotFound("category to delete with id '$categoryId' not found")
        if (category.userId != userId) {
            throw AcceptError("You dont have access rights to delete this category")
        }
        categoryRepository.deleteCategory(categoryId)
    }

}