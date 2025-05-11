package com.bortnik.todo.usecase.category

import com.bortnik.todo.domain.dto.CategoryCreate
import com.bortnik.todo.domain.entities.Category
import com.bortnik.todo.domain.exceptions.category.CategoryAlreadyExists
import com.bortnik.todo.domain.repositories.CategoryRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class CreateCategoryUseCaseTests {
    private val repo = mockk<CategoryRepository>()
    private val useCase = CreateCategoryUseCase(repo)

    @Test
    fun `addCategory should throws CategoryAlreadyExists when category already exists`() {
        val categoryId = 1
        val userId = 1
        val name = "name"
        val mockCategory = Category(categoryId, userId, name)

        every { repo.getCategoryByUserIdAndName(userId, name) } returns mockCategory

        assertThrows<CategoryAlreadyExists> {
            useCase.addCategory(CategoryCreate(userId, name))
        }
    }

    @Test
    fun `addCategory should create category if the category doesn't exists`() {
        val userId = 1
        val name = "name"
        val categoryCreate = CategoryCreate(userId, name)
        val resultCategory = Category(1, userId, name)

        every { repo.getCategoryByUserIdAndName(userId, name) } returns null
        every { repo.addCategory(categoryCreate) } returns resultCategory

        assertEquals(resultCategory, useCase.addCategory(categoryCreate))

        verifyOrder {
            repo.getCategoryByUserIdAndName(userId, name)
            repo.addCategory(categoryCreate)
        }
    }
}