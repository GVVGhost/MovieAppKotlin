package com.gvvghost.movieappkotlin.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gvvghost.movieappkotlin.api.ApiFactory
import com.gvvghost.movieappkotlin.database.AppDatabase
import com.gvvghost.movieappkotlin.pojo.AuthorReview
import com.gvvghost.movieappkotlin.pojo.Movie
import com.gvvghost.movieappkotlin.pojo.MovieVideo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MovieDetailViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "MovieDetailViewModel"
    }

    private val appDao = AppDatabase.getInstance(application).appDao()
    private val compositeDisposable = CompositeDisposable()
    private val error: MutableLiveData<String> = MutableLiveData<String>()
    private val videos: MutableLiveData<List<MovieVideo>> = MutableLiveData<List<MovieVideo>>()
    private val reviews: MutableLiveData<List<AuthorReview>> = MutableLiveData<List<AuthorReview>>()

    fun getError(): LiveData<String> = error
    fun getVideos(): LiveData<List<MovieVideo>> = videos
    fun getReviews(): LiveData<List<AuthorReview>> = reviews
    fun getFavoriteMovies(id: Int): LiveData<Movie> = appDao.getFavoriteMovie(id)

    fun loadVideos(id: Int) {
        val disposable = ApiFactory.apiService.loadMovieDetails(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    videos.value = it.movieVideos
                }, {
                    Log.d(TAG, "loadVideos: ${it.message}")
                }
            )
        compositeDisposable.add(disposable)
    }

    fun loadReviews(id: Int) {
        val disposable = ApiFactory.apiService.loadMovieReviews(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    reviews.value = it.movieReviews
                }, {
                    Log.d(TAG, "loadReviews: ${it.message}")
                }
            )
        compositeDisposable.add(disposable)
    }

    fun insertMovie(movie: Movie) {
        val disposable = appDao.insertMovie(movie)
            .subscribeOn(Schedulers.io())
            .subscribe() //TODO exception handler in subscribe method
        compositeDisposable.add(disposable)
    }

    fun removeMovie(movieId: Int) {
        val disposable = appDao.removeMovie(movieId)
            .subscribeOn(Schedulers.io())
            .subscribe() //TODO exception handler in subscribe method
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}