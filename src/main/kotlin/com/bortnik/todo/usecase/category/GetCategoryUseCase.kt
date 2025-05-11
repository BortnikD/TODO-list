package com.bortnik.todo.usecase.category

import com.bortnik.todo.domain.dto.PaginatedResponse
import com.bortnik.todo.domain.entities.Category
import com.bortnik.todo.domain.repositories.CategoryRepository
import com.bortnik.todo.usecase.generatePagesLinks
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class GetCategoryUseCase(
    private val categoryRepository: CategoryRepository,
    @Value("\${base-url}") private val baseUrl: String
) {

    @Cacheable(value = ["category.byId"], key = "#categoryId")
    fun getCategoryById(categoryId: Int): Category? {
        return categoryRepository.getCategoryById(categoryId)
    }

    @Cacheable(value = ["categories.byUserId"], key = "#userId + '_' + #offset + '_' + #limit")
    fun getPaginatedUserCategories(userId: Int, offset: Long, limit: Int): PaginatedResponse<Category> {
        val count = categoryRepository.getCount(userId)
        val basePath = "$baseUrl/categories/my?"
        val pagesLinks = generatePagesLinks(offset, limit, count, basePath)
        return PaginatedResponse(
            count = count,
            previousPage = pagesLinks.first,
            nextPage = pagesLinks.second,
            results = categoryRepository.getUserCategories(userId, offset, limit)
        )
    }
}
