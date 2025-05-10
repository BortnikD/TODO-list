package com.bortnik.todo.usecase.category

import com.bortnik.todo.domain.dto.CategoryCreate
import com.bortnik.todo.domain.entities.Category
import com.bortnik.todo.domain.exceptions.category.CategoryAlreadyExists
import com.bortnik.todo.domain.repositories.CategoryRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service

@Service
class CreateCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {

    @CacheEvict(value = ["categories.byUserId"], allEntries = true)
    fun addCategory(category: CategoryCreate): Category {
        if (categoryRepository.getCategoryByUserIdAndName(category.userId, category.name) != null) {
            throw CategoryAlreadyExists("category with name '${category.name}' already exists")
        }
        return categoryRepository.addCategory(category)
    }
}
