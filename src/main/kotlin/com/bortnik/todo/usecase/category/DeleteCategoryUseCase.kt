package com.bortnik.todo.usecase.category

import com.bortnik.todo.domain.repositories.CategoryRepository
import org.springframework.stereotype.Service

@Service
class DeleteCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {

    fun deleteCategory(categoryId: Int) {
        categoryRepository.deleteCategory(categoryId)
    }

}