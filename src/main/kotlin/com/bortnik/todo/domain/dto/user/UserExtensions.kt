package com.bortnik.todo.domain.dto.user

import com.bortnik.todo.domain.entities.User

fun User.toPublic() =
    UserPublic(
        id = this.id,
        username = this.username,
        email = this.email
    )