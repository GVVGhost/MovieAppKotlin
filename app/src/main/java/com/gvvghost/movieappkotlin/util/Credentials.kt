package com.gvvghost.movieappkotlin.util

import android.util.Patterns
import com.gvvghost.movieappkotlin.util.Constants.MIN_PASSWORD_LENGTH

object Credentials {
    fun isUserNameNotValid(username: String): Boolean =
        if (username.contains("@")) !Patterns.EMAIL_ADDRESS.matcher(username).matches()
        else username.trim().isEmpty()

    fun isPasswordNotValid(password: String): Boolean =
        password.trim().length <= MIN_PASSWORD_LENGTH
}