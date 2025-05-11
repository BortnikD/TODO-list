package com.bortnik.todo.usecase

fun generatePagesLinks(
    offset: Long,
    limit: Int,
    count: Long,
    basePath: String
): Pair<String?, String?> {

    fun buildUrl(newOffset: Long): String {
        val encodedPath = if (basePath.contains("?")) {
            "${basePath}offset=$newOffset&limit=$limit"
        } else {
            "${basePath}offset=$newOffset&limit=$limit"
        }
        return encodedPath
    }

    val previousPage = if (offset > 0) buildUrl(maxOf(0, offset - limit)) else null
    val nextPage = if (offset + limit < count) buildUrl(offset + limit) else null

    return previousPage to nextPage
}