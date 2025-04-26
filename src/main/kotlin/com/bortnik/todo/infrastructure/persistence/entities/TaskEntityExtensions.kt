package com.bortnik.todo.infrastructure.persistence.entities

import com.bortnik.todo.domain.entities.Task

fun TaskEntity.toDomain() =
    Task(
        id = id.value,
        categoryId = this.categoryId.value,
        priority = this.priority,
        text = this.text,
        createdAt = this.createdAt,
        isCompleted = this.isCompleted,
        completedAt = this.completedAt
    )