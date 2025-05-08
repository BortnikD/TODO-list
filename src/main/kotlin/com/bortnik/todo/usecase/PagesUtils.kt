package com.bortnik.todo.usecase

fun generatePagesLinks(
    offset: Long,
    limit: Int,
    count: Long,
    path: String
): Pair<String?, String?> {

    val previousPage = if (offset - limit < 0) {
        null
    } else {
        "$path&offset=${offset - limit}&limit=$limit"
    }

    val nextPage = if (offset + limit > count) {
        null
    } else {
        "$path&offset=${offset + limit}&limit=$limit"
    }

    return Pair(previousPage, nextPage)
}