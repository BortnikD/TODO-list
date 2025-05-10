package com.bortnik.todo.domain.repositories

import com.bortnik.todo.domain.dto.CategoryCreate
import com.bortnik.todo.domain.entities.Category

interface CategoryRepository {

    fun addCategory(category: CategoryCreate): Category

    fun getCount(userId: Int): Long

    fun getCategoryByUserIdAndName(userId: Int, name: String): Category?

    fun getUserCategories(userId: Int, offset: Long, limit: Int): List<Category>?

    fun getCategoryById(categoryId: Int): Category?

    fun deleteCategory(categoryId: Int)
}