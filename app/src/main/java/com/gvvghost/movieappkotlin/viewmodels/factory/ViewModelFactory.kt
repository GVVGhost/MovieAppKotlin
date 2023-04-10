package com.gvvghost.movieappkotlin.viewmodels.factory

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gvvghost.movieappkotlin.api.ApiHelper
import com.gvvghost.movieappkotlin.database.DatabaseHelper
import com.gvvghost.movieappkotlin.viewmodels.ContentViewModel
import com.gvvghost.movieappkotlin.viewmodels.LoginViewModel
import com.gvvghost.movieappkotlin.viewmodels.MovieDetailViewModel

class ViewModelFactory(
    private val apiHelper: ApiHelper,
    private val databaseHelper: DatabaseHelper,
    private val sharedPreferences: SharedPreferences
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java))
            return LoginViewModel(apiHelper, databaseHelper, sharedPreferences) as T
        if (modelClass.isAssignableFrom(ContentViewModel::class.java))
            return ContentViewModel(apiHelper, databaseHelper, sharedPreferences) as T
        if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java))
            return MovieDetailViewModel(apiHelper, databaseHelper, sharedPreferences) as T
        throw IllegalArgumentException("Unknown class name")
    }
}