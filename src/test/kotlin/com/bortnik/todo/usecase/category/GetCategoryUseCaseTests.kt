package com.bortnik.todo.usecase.category

import com.bortnik.todo.domain.dto.PaginatedResponse
import com.bortnik.todo.domain.entities.Category
import com.bortnik.todo.domain.repositories.CategoryRepository
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

class GetCategoryUseCaseTests {
    private val repo = mockk<CategoryRepository>()
    private val useCase = GetCategoryUseCase(repo, "http://localhost:8080")

    @Test
    fun `getPaginatedUserCategories should return all categories when limit equals total count`() {
        val categoriesList = (1..20).map { Category(it, 1, "some_name $it") }

        every { repo.getCount(1) } returns 20
        every { repo.getUserCategories(1, 0, 20) } returns categoriesList

        val expected = PaginatedResponse(
            count = 20,
            previousPage = null,
            nextPage = null,
            results = categoriesList
        )

        assertEquals(expected, useCase.getPaginatedUserCategories(1, 0, 20))
    }

    @Test
    fun `getPaginatedUserCategories should return first page with next link`() {
        val categoriesList = (1..20).map { Category(it, 1, "some_name $it") }

        every { repo.getCount(1) } returns 20
        every { repo.getUserCategories(1, 0, 5) } returns categoriesList.take(5)

        val expected = PaginatedResponse(
            count = 20,
            previousPage = null,
            nextPage = "http://localhost:8080/categories/my?offset=5&limit=5",
            results = categoriesList.take(5)
        )

        assertEquals(expected, useCase.getPaginatedUserCategories(1, 0, 5))
    }

    @Test
    fun `getPaginatedUserCategories should return empty response for non-existent user`() {
        every { repo.getCount(2) } returns 0
        every { repo.getUserCategories(2, 0, 5) } returns null

        val expected = PaginatedResponse<Category>(
            count = 0,
            previousPage = null,
            nextPage = null,
            results = null
        )

        assertEquals(expected, useCase.getPaginatedUserCategories(2, 0, 5))
    }

    @Test
    fun `getPaginatedUserCategories should return empty list for user`() {
        every { repo.getCount(2) } returns 0
        every { repo.getUserCategories(2, 0, 5) } returns listOf()

        val expected = PaginatedResponse<Category>(
            count = 0,
            previousPage = null,
            nextPage = null,
            results = listOf()
        )

        assertEquals(expected, useCase.getPaginatedUserCategories(2, 0, 5))
    }

    @Test
    fun `getPaginatedUserCategories should return last page without next link`() {
        val categoriesList = (1..20).map { Category(it, 1, "some_name $it") }

        every { repo.getCount(1) } returns 20
        every { repo.getUserCategories(1, 15, 5) } returns categoriesList.takeLast(5)

        val expected = PaginatedResponse(
            count = 20,
            previousPage = "http://localhost:8080/categories/my?offset=10&limit=5",
            nextPage = null,
            results = categoriesList.takeLast(5)
        )

        assertEquals(expected, useCase.getPaginatedUserCategories(1, 15, 5))
    }

    @Test
    fun `getPaginatedUserCategories should return middle page with prev and next links`() {
        val categoriesList = (1..20).map { Category(it, 1, "some_name $it") }

        every { repo.getCount(1) } returns 20
        every { repo.getUserCategories(1, 5, 5) } returns categoriesList.slice(5..9)

        val expected = PaginatedResponse(
            count = 20,
            previousPage = "http://localhost:8080/categories/my?offset=0&limit=5",
            nextPage = "http://localhost:8080/categories/my?offset=10&limit=5",
            results = categoriesList.slice(5..9)
        )

        assertEquals(expected, useCase.getPaginatedUserCategories(1, 5, 5))
    }

    @Test
    fun `get category by id return task or null`() {
        val category = Category(1, 1, "some_name")
        every { repo.getCategoryById(1) } returns category
        every { repo.getCategoryById(2) } returns null

        assertEquals(category, useCase.getCategoryById(1))
        assertEquals(null, useCase.getCategoryById(2))
    }
}