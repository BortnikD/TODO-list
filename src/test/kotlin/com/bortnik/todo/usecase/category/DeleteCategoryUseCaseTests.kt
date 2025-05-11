package com.bortnik.todo.usecase.category

import com.bortnik.todo.domain.entities.Category
import com.bortnik.todo.domain.exceptions.category.CategoryNotFound
import com.bortnik.todo.domain.exceptions.user.AcceptError
import com.bortnik.todo.domain.repositories.CategoryRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteCategoryUseCaseTest {

    private val categoryRepository: CategoryRepository = mockk()
    private val deleteCategoryUseCase = DeleteCategoryUseCase(categoryRepository)

    @Test
    fun `deleteCategory should delete category when it exists and belongs to user`() {
        val categoryId = 1
        val userId = 1
        val mockCategory = Category(1, 1, "name")

        every { categoryRepository.getCategoryById(categoryId) } returns mockCategory
        every { categoryRepository.deleteCategory(categoryId) } returns Unit

        deleteCategoryUseCase.deleteCategory(categoryId, userId)

        verify(exactly = 1) { categoryRepository.getCategoryById(categoryId) }
        verify(exactly = 1) { categoryRepository.deleteCategory(categoryId) }
    }

    @Test
    fun `deleteCategory should throw CategoryNotFound when category does not exist`() {
        val categoryId = 1
        val userId = 1

        every { categoryRepository.getCategoryById(categoryId) } returns null

        assertThrows<CategoryNotFound> {
            deleteCategoryUseCase.deleteCategory(categoryId, userId)
        }

        verify(exactly = 1) { categoryRepository.getCategoryById(categoryId) }
        verify(exactly = 0) { categoryRepository.deleteCategory(any()) }
    }

    @Test
    fun `deleteCategory should throw AcceptError when category does not belong to user`() {
        val categoryId = 1
        val ownerUserId = 1
        val anotherUserId = 2
        val mockCategory = Category(categoryId, ownerUserId, "name")


        every { categoryRepository.getCategoryById(categoryId) } returns mockCategory

        assertThrows<AcceptError> {
            deleteCategoryUseCase.deleteCategory(categoryId, anotherUserId)
        }

        verify(exactly = 1) { categoryRepository.getCategoryById(categoryId) }
        verify(exactly = 0) { categoryRepository.deleteCategory(any()) }
    }
}