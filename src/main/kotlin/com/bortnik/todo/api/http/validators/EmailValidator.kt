package com.bortnik.todo.api.http.validators

object EmailValidator {
    private val emailRegex = Regex(
        "^(?=.{1,254}\$)(?=.{1,64}@)[-!#\$%&'*+/0-9=?A-Z^_`a-z{|}~]+" +
                "(\\.[-!#\$%&'*+/0-9=?A-Z^_`a-z{|}~]+)*@" +
                "[A-Za-z0-9]([A-Za-z0-9-]{0,61}[A-Za-z0-9])?" +
                "(\\.[A-Za-z0-9]([A-Za-z0-9-]{0,61}[A-Za-z0-9])?)*" +
                "\\.[A-Za-z]{2,}\$"
    )

    fun isValid(email: String): Boolean = emailRegex.matches(email)
}