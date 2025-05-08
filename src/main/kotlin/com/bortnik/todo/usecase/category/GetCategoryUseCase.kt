package com.bortnik.todo.usecase.category

import com.bortnik.todo.domain.dto.PaginatedResponse
import com.bortnik.todo.domain.entities.Category
import com.bortnik.todo.domain.repositories.CategoryRepository
import com.bortnik.todo.usecase.generatePagesLinks
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GetCategoryUseCase(
    private val categoryRepository: CategoryRepository,
    @Value("\${base-url}") private val baseUrl: String
) {

    private fun getUserCategories(userId: Int, offset: Long, limit: Int): List<Category>? {
        return categoryRepository.getUserCategories(userId, offset, limit)
    }

    fun getCategoryById(categoryId: Int): Category? {
        return categoryRepository.getCategoryById(categoryId)
    }

    fun getPaginatedUserCategories(userId: Int, offset: Long, limit: Int): PaginatedResponse<Category> = transaction {
        val count = categoryRepository.getCount(userId)
        val basePath = "$baseUrl/categories/my?"
        val pagesLinks = generatePagesLinks(offset, limit, count, basePath)
        PaginatedResponse(
            count = count,
            previousPage = pagesLinks.first,
            nextPage = pagesLinks.second,
            results = getUserCategories(userId, offset, limit)
        )
    }
}
