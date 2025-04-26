package com.bortnik.todo.usecase.category

import com.bortnik.todo.domain.dto.CategoryCreate
import com.bortnik.todo.domain.entities.Category
import com.bortnik.todo.domain.repositories.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CreateCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {

    fun addCategory(category: CategoryCreate): Category {
        return categoryRepository.addCategory(category)
    }
}
