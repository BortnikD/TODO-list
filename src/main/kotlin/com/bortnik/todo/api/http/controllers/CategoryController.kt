package com.bortnik.todo.api.http.controllers

import com.bortnik.todo.domain.dto.CategoryCreate
import com.bortnik.todo.domain.entities.Category
import com.bortnik.todo.domain.exceptions.CategoryNotFound
import com.bortnik.todo.domain.exceptions.InvalidRequestField
import com.bortnik.todo.usecase.category.CreateCategoryUseCase
import com.bortnik.todo.usecase.category.DeleteCategoryUseCase
import com.bortnik.todo.usecase.category.GetCategoryUseCase
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
    private val getCategoryUseCase: GetCategoryUseCase
) {

    @PostMapping
    fun addCategory(@RequestBody category: CategoryCreate): Category {
        return createCategoryUseCase.addCategory(category)
    }

    @DeleteMapping("/{categoryId}")
    fun deleteCategory(@PathVariable categoryId: Int) {
        if (categoryId <= 0) throw InvalidRequestField("category id must be greet then 0")
        deleteCategoryUseCase.deleteCategory(categoryId)
    }

    @GetMapping("/{categoryId}")
    fun getCategoryById(@PathVariable categoryId: Int): Category {
        if (categoryId <= 0) throw InvalidRequestField("category id must be greet then 0")
        return getCategoryUseCase.getCategoryById(categoryId) ?: throw CategoryNotFound("category not found")
    }
}