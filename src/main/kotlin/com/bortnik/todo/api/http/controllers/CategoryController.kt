package com.bortnik.todo.api.http.controllers

import com.bortnik.todo.domain.dto.CategoryCreate
import com.bortnik.todo.api.http.dto.CategoryCreateRequest
import com.bortnik.todo.api.http.openapi.controllers.CategoryApiDocs
import com.bortnik.todo.domain.dto.PaginatedResponse
import com.bortnik.todo.domain.entities.Category
import com.bortnik.todo.domain.exceptions.InvalidRequestField
import com.bortnik.todo.infrastructure.security.user.getUserId
import com.bortnik.todo.usecase.category.CreateCategoryUseCase
import com.bortnik.todo.usecase.category.DeleteCategoryUseCase
import com.bortnik.todo.usecase.category.GetCategoryUseCase
import com.bortnik.todo.usecase.user.GetUserUseCase
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/categories")
class CategoryController(
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val getUserUseCase: GetUserUseCase
): CategoryApiDocs {

    @PostMapping
    override fun addCategory(
        @RequestBody category: CategoryCreateRequest,
        @AuthenticationPrincipal user: UserDetails
    ): Category {
        val userId = user.getUserId(getUserUseCase)

        val categoryWithUserId = CategoryCreate(userId, category.name)
        return createCategoryUseCase.addCategory(categoryWithUserId)
    }

    @DeleteMapping("/{categoryId}")
    override fun deleteCategory(
        @PathVariable categoryId: Int,
        @AuthenticationPrincipal user: UserDetails
    ) {
        if (categoryId <= 0) throw InvalidRequestField("category id must be greet 0")

        val userId = user.getUserId(getUserUseCase)

        deleteCategoryUseCase.deleteCategory(categoryId, userId)
    }

    @GetMapping("/my")
    override fun getUserCategories(
        @RequestParam offset: Long?,
        @RequestParam limit: Int?,
        @AuthenticationPrincipal user: UserDetails
    ): PaginatedResponse<Category> {
        validatePagination(offset, limit)
        val userId = user.getUserId(getUserUseCase)

        return getCategoryUseCase.getPaginatedUserCategories(userId, offset ?: 0, limit ?: 10)
    }

    fun validatePagination(offset: Long?, limit: Int?) {
        offset?.let { value ->
            if (value < 0) {
                throw InvalidRequestField("offset value must by greet or equal 0")
            }
        }
        limit?.let { value ->
            if (value < 0) {
                throw InvalidRequestField("offset value must by greet or equal 0")
            }
        }
    }
}