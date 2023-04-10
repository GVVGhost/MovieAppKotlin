package com.gvvghost.movieappkotlin.util

import android.content.Context
import android.widget.Toast

object UIMessages {
    fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        if (message.isNotBlank()) Toast.makeText(context, message, duration).show()
    }
}