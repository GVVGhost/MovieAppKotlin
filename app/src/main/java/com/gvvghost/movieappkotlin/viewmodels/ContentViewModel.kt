package com.gvvghost.movieappkotlin.viewmodels

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gvvghost.movieappkotlin.LoginActivity
import com.gvvghost.movieappkotlin.LoginActivity.Companion.EMAIL
import com.gvvghost.movieappkotlin.LoginActivity.Companion.IS_LOGGED_IN
import com.gvvghost.movieappkotlin.LoginActivity.Companion.PASSWORD
import com.gvvghost.movieappkotlin.api.ApiFactory
import com.gvvghost.movieappkotlin.database.AppDao
import com.gvvghost.movieappkotlin.database.AppDatabase
import com.gvvghost.movieappkotlin.pojo.Movie
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ContentViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "ContentViewModel"
    }

    private var page = 1
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val appDao: AppDao = AppDatabase.getInstance(application).appDao()
    private val isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val movies: MutableLiveData<List<Movie>> = MutableLiveData<List<Movie>>()
    private val sPref: SharedPreferences = application
        .getSharedPreferences(LoginActivity.MY_PREF, AppCompatActivity.MODE_PRIVATE)

    init {
        isLoading.value = false
        loadMovies()
    }

    fun getMovies(): LiveData<List<Movie>> = movies
    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun isMovieListEmpty():Boolean {
        movies.value?.run { if (this.isNotEmpty()) return false }
        return true
    }

    fun loadMovies(addToExistList: Boolean = true) {
        if (isLoading.value != null && isLoading.value == false) {
            val disposable = ApiFactory.apiService.loadMovies(page = page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { isLoading.postValue(true) }
                .doAfterTerminate { isLoading.postValue(false) }
                .subscribe(
                    {
                        it.loadedMovies?.apply {
                            if (addToExistList && movies.value != null) {
                                val listOfMovies = movies.value as MutableList
                                listOfMovies.addAll(this)
                                movies.postValue(listOfMovies)
                            } else movies.postValue(it.loadedMovies)
                            Log.d(TAG, "loaded page: $page")
                            page++
                        }
                    },
                    { Log.d(TAG, "loadMovies, failed: ${it.message}") }
                )
            compositeDisposable.add(disposable)
        }
    }

    fun loadMarkedMovies() {
        if (isLoading.value != null && isLoading.value == true) return
        page = 1
        val disposable = appDao.getAllFavoriteMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { isLoading.value = true }
            .doAfterTerminate { isLoading.value = false }
            .subscribe({
                movies.value = it
            }, {
                Log.d(TAG, "loadMarkedMovies, failed: ${it.message}")
            })
        compositeDisposable.add(disposable)
    }

    fun logout() {
        sPref.edit()
            .putBoolean(IS_LOGGED_IN, false)
            .remove(PASSWORD)
            .remove(EMAIL)
            .apply()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}