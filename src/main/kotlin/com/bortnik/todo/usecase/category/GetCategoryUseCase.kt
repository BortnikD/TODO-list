package com.bortnik.todo.usecase.category

import com.bortnik.todo.domain.entities.Category
import com.bortnik.todo.domain.repositories.CategoryRepository
import org.springframework.stereotype.Service

@Service
class GetCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {

    fun getCategoryById(categoryId: Int): Category? {
        return categoryRepository.getCategoryById(categoryId)
    }
}
