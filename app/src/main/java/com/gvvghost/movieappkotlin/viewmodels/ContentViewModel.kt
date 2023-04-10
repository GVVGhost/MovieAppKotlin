package com.gvvghost.movieappkotlin.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gvvghost.movieappkotlin.api.ApiHelper
import com.gvvghost.movieappkotlin.database.DatabaseHelper
import com.gvvghost.movieappkotlin.pojo.Movie
import com.gvvghost.movieappkotlin.util.Constants.EMAIL
import com.gvvghost.movieappkotlin.util.Constants.IS_LOGGED_IN
import com.gvvghost.movieappkotlin.util.Constants.LANG_ENG
import com.gvvghost.movieappkotlin.util.Constants.PASSWORD
import com.gvvghost.movieappkotlin.util.Constants.SORT_DESC
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ContentViewModel(
    private val apiHelper: ApiHelper,
    private val databaseHelper: DatabaseHelper,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    companion object {
        private const val TAG = "ContentViewModel"
    }

    private var page = 1
    private val isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val movies: MutableLiveData<List<Movie>> = MutableLiveData<List<Movie>>()

    init {
        isLoading.value = false
        fetchMovies()
    }

    fun getMovies(): LiveData<List<Movie>> = movies

    fun getIsLoading(): LiveData<Boolean> = isLoading

    fun isMovieListEmpty(): Boolean {
        movies.value?.run { if (this.isNotEmpty()) return false }
        return true
    }

    fun fetchMovies(addToExistList: Boolean = true) {
        if (isLoading.value == null || isLoading.value == true) return
        viewModelScope.launch {
            isLoading.postValue(true)
            try {
                coroutineScope {
                    val deferred = async {
                        apiHelper.loadMovies(page, LANG_ENG, SORT_DESC, 4)
                    }
                    val fetchedMovies = deferred.await()
                    val currentList = mutableListOf<Movie>()
                    if (addToExistList) movies.value?.let { currentList.addAll(it) }
                    fetchedMovies.movieList?.let { currentList.addAll(it) }
                    movies.postValue(currentList)
                    Log.d(TAG, "loaded page: $page")
                    page++
                }
            } catch (e: Exception) {
                Log.d(TAG, "fetchMovies: ${e.message}")
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    fun fetchMarkedMovies() {
        if (isLoading.value == null || isLoading.value == true) return
        page = 1
        viewModelScope.launch {
            isLoading.postValue(true)
            try {
                val fetchedMovies = databaseHelper.getAllFavoriteMovies()
                movies.postValue(fetchedMovies)
            } catch (e: Exception) {
                Log.d(TAG, "fetchMarkedMovies: ${e.message}")
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    fun logout() {
        sharedPreferences.edit()
            .putBoolean(IS_LOGGED_IN, false)
            .remove(PASSWORD)
            .remove(EMAIL)
            .apply()
    }
}