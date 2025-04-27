package com.bortnik.todo.infrastructure.persistence.entities.category

import com.bortnik.todo.domain.entities.Category

fun CategoryEntity.toDomain() =
    Category(
        id = id.value,
        name = this.name
    )