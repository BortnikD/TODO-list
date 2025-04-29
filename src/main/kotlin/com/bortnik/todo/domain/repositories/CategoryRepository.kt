package com.bortnik.todo.domain.repositories

import com.bortnik.todo.domain.dto.CategoryCreate
import com.bortnik.todo.domain.entities.Category

interface CategoryRepository {

    fun addCategory(category: CategoryCreate): Category

    fun getCategoryById(categoryId: Int): Category?

    fun deleteCategory(categoryId: Int, userId: Int)
}