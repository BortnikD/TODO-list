package com.bortnik.todo.usecase.category

import com.bortnik.todo.domain.dto.CategoryUpdate
import com.bortnik.todo.domain.entities.Category
import com.bortnik.todo.domain.exceptions.category.CategoryNotFound
import com.bortnik.todo.domain.exceptions.auth.AcceptError
import com.bortnik.todo.domain.repositories.CategoryRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class UpdateCategoryUseCaseTests {

    private val repo = mockk<CategoryRepository>()
    private val useCase = UpdateCategoryUseCase(repo)

    private val id = 1
    private val userId = 1
    private val name = "name"
    private val newName = "newName"

    private val category = Category(id, userId, name)
    private val categoryUpdate = CategoryUpdate(id, userId, newName)

    @Test
    fun `update should update category when credentials are valid`() {
        val updatedCategory = category.copy(name = categoryUpdate.name)

        every { repo.getCategoryById(categoryUpdate.id) } returns category
        every { repo.updateCategory(categoryUpdate) } returns updatedCategory

        assertEquals(updatedCategory, useCase.update(categoryUpdate))
    }

    @Test
    fun `update should throws CategoryNotFound when category doesn't exists`() {
        every { repo.getCategoryById(categoryUpdate.id) } returns null

        val except = assertThrows<CategoryNotFound> {
            useCase.update(categoryUpdate)
        }

        assertEquals("Category to update with id '${categoryUpdate.id}' not found", except.message)
    }

    @Test
    fun `update should throw AcceptError when CategoryUpdate user id doesn't match real Category user id`() {
        val otherUserId = 1234

        every { repo.getCategoryById(categoryUpdate.id) } returns category

        val except = assertThrows<AcceptError> {
            useCase.update(categoryUpdate.copy(userId = otherUserId))
        }

        assertEquals("You dont have access rights to update this category", except.message)
    }
}