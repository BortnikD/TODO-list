package com.bortnik.todo.api.http.controllers

import com.bortnik.todo.domain.dto.CategoryCreate
import com.bortnik.todo.api.http.dto.CategoryCreateRequest
import com.bortnik.todo.domain.entities.Category
import com.bortnik.todo.domain.exceptions.category.CategoryNotFound
import com.bortnik.todo.domain.exceptions.InvalidRequestField
import com.bortnik.todo.infrastructure.security.user.getUserId
import com.bortnik.todo.usecase.category.CreateCategoryUseCase
import com.bortnik.todo.usecase.category.DeleteCategoryUseCase
import com.bortnik.todo.usecase.category.GetCategoryUseCase
import com.bortnik.todo.usecase.user.GetUserUseCase
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/categories")
class CategoryController(
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val getUserUseCase: GetUserUseCase
) {

    @PostMapping
    fun addCategory(
        @RequestBody category: CategoryCreateRequest,
        @AuthenticationPrincipal user: UserDetails
    ): Category {
        val userId = user.getUserId(getUserUseCase)

        val categoryWithUserId = CategoryCreate(userId, category.name)
        return createCategoryUseCase.addCategory(categoryWithUserId)
    }

    @DeleteMapping("/{categoryId}")
    fun deleteCategory(
        @PathVariable categoryId: Int,
        @AuthenticationPrincipal user: UserDetails
    ) {
        if (categoryId <= 0) throw InvalidRequestField("category id must be greet then 0")

        val userId = user.getUserId(getUserUseCase)

        deleteCategoryUseCase.deleteCategory(categoryId, userId)
    }

    @GetMapping("/my")
    fun getUserCategories(@AuthenticationPrincipal user: UserDetails): List<Category> {
        val userId = user.getUserId(getUserUseCase)

        return getCategoryUseCase.getUserCategories(userId) ?: throw CategoryNotFound("categories not found")
    }
}