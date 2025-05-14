package com.bortnik.todo.api

import com.bortnik.todo.api.http.controllers.CategoryController
import com.bortnik.todo.api.http.dto.CategoryCreateRequest
import com.bortnik.todo.api.http.dto.CategoryUpdateRequest
import com.bortnik.todo.api.http.exceptions.BadCredentials
import com.bortnik.todo.domain.dto.CategoryCreate
import com.bortnik.todo.domain.dto.CategoryUpdate
import com.bortnik.todo.domain.entities.Category
import com.bortnik.todo.api.http.exceptions.InvalidRequestField
import com.bortnik.todo.infrastructure.security.user.getUserId
import com.bortnik.todo.usecase.category.CreateCategoryUseCase
import com.bortnik.todo.usecase.category.DeleteCategoryUseCase
import com.bortnik.todo.usecase.category.GetCategoryUseCase
import com.bortnik.todo.usecase.category.UpdateCategoryUseCase
import com.bortnik.todo.usecase.user.GetUserUseCase
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class CategoryControllerTests {

    private val createCategoryUseCase = mockk<CreateCategoryUseCase>()
    private val deleteCategoryUseCase = mockk<DeleteCategoryUseCase>()
    private val getCategoryUseCase = mockk<GetCategoryUseCase>()
    private val updateCategoryUseCase = mockk<UpdateCategoryUseCase>()
    private val getUserUseCase = mockk<GetUserUseCase>()
    private val controller = CategoryController(
        createCategoryUseCase,
        deleteCategoryUseCase,
        getCategoryUseCase,
        updateCategoryUseCase,
        getUserUseCase
    )

    private val validCategoryCreateRequestName = "name"
    private val validCategoryCreateRequest = CategoryCreateRequest(validCategoryCreateRequestName)
    private val userId = 1
    private val username = "username"
    private val userDetails = org.springframework.security.core.userdetails.User(
        username, "encodedPassword", listOf()
    )

    @Test
    fun `addCategory should create category when credentials are valid`() {
        val validCategoryCreate = CategoryCreate(
            userId,
            validCategoryCreateRequestName
        )

        every { userDetails.getUserId(getUserUseCase) } returns userId
        every { createCategoryUseCase.addCategory(validCategoryCreate) } returns mockk()

        controller.addCategory(validCategoryCreateRequest, userDetails)
    }

    @Test
    fun `addCategory should throws BadCredentials when category name is incorrect`() {
        val wrongUsername = "n"
        val except = assertThrows<BadCredentials> {
            controller.addCategory(validCategoryCreateRequest.copy(name = wrongUsername), userDetails)
        }

        assertEquals("category name is too short or long", except.message)
    }

    @Test
    fun `deleteCategory should delete category when category id is correct`() {
        val categoryId = 1

        every { userDetails.getUserId(getUserUseCase) } returns userId
        every { deleteCategoryUseCase.deleteCategory(categoryId, userId) } returns Unit

        controller.deleteCategory(categoryId, userDetails)
    }

    @Test
    fun `deleteCategory should throws InvalidRequestField when category id is incorrect`() {
        val categoryId = -1

        val except = assertThrows<InvalidRequestField> {
            controller.deleteCategory(categoryId, userDetails)
        }

        assertEquals("category id must be greater to 0", except.message)
    }

    @Test
    fun `getUserCategories should return response when offset and limit are valid`() {
        val offset = 0L
        val limit = 10

        every { userDetails.getUserId(getUserUseCase) } returns userId
        every {
            getCategoryUseCase.getPaginatedUserCategories(userId, offset, limit)
        } returns mockk()

        controller.getUserCategories(offset, limit, userDetails)

    }

    @Test
    fun `getUserCategories should throws when offset is invalid`() {
        val offset = -1L
        val limit = 10

        every { userDetails.getUserId(getUserUseCase) } returns userId
        every {
            getCategoryUseCase.getPaginatedUserCategories(userId, offset, limit)
        } returns mockk()

        val except = assertThrows<InvalidRequestField> {
            controller.getUserCategories(offset, limit, userDetails)
        }

        assertEquals("offset value must by greater or equal to 0", except.message)
    }

    @Test
    fun `getUserCategories should throws when limit is invalid`() {
        val offset = 0L
        val limit = -10

        every { userDetails.getUserId(getUserUseCase) } returns userId
        every {
            getCategoryUseCase.getPaginatedUserCategories(userId, offset, limit)
        } returns mockk()

        val except = assertThrows<InvalidRequestField> {
            controller.getUserCategories(offset, limit, userDetails)
        }

        assertEquals("offset value must by greater or equal to 0", except.message)
    }

    private val categoryUpdateRequestId = 1
    private val categoryUpdateRequestName = "name"
    private val validCategoryUpdateRequest = CategoryUpdateRequest(
        categoryUpdateRequestId,
        categoryUpdateRequestName
    )
    private val updatedCategory = Category(
        categoryUpdateRequestId,
        userId,
        categoryUpdateRequestName
    )

    @Test
    fun `updateUserCategories should update category when credentials are valid`() {
        val validCategoryUpdate = CategoryUpdate(
            categoryUpdateRequestId,
            userId,
            categoryUpdateRequestName
        )

        every { userDetails.getUserId(getUserUseCase) } returns userId
        every { updateCategoryUseCase.update(validCategoryUpdate) } returns updatedCategory

        assertEquals(updatedCategory, controller.updateUserCategories(validCategoryUpdateRequest, userDetails))
    }

    @Test
    fun `updateUserCategories should throws when category name is invalid`() {
        val invalidName = "n"
        val invalidCategoryUpdateRequest = validCategoryUpdateRequest.copy(name = invalidName)

        every { userDetails.getUserId(getUserUseCase) } returns userId

        val except = assertThrows<BadCredentials> {
            controller.updateUserCategories(invalidCategoryUpdateRequest, userDetails)
        }

        assertEquals("category name is too short or long", except.message)
    }
}