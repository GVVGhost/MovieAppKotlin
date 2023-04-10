package com.gvvghost.movieappkotlin.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gvvghost.movieappkotlin.api.ApiHelper
import com.gvvghost.movieappkotlin.database.DatabaseHelper
import com.gvvghost.movieappkotlin.pojo.AuthorReview
import com.gvvghost.movieappkotlin.pojo.Movie
import com.gvvghost.movieappkotlin.pojo.MovieVideo
import com.gvvghost.movieappkotlin.util.Constants.LANG_ENG
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val apiHelper: ApiHelper,
    private val databaseHelper: DatabaseHelper,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    companion object {
        private const val TAG = "MovieDetailViewModel"
    }

    private val error: MutableLiveData<String> = MutableLiveData<String>()
    private val videos: MutableLiveData<List<MovieVideo>> = MutableLiveData<List<MovieVideo>>()
    private val reviews: MutableLiveData<List<AuthorReview>> = MutableLiveData<List<AuthorReview>>()
    private val currentMovie: MutableLiveData<Movie> = MutableLiveData<Movie>()
    private val movieInDB: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun getError(): LiveData<String> = error
    fun getVideos(): LiveData<List<MovieVideo>> = videos
    fun getReviews(): LiveData<List<AuthorReview>> = reviews
    fun getMovie(): LiveData<Movie?> = currentMovie
    fun isMovieInDB(): LiveData<Boolean> = movieInDB
    init {
        movieInDB.value = false
    }

    fun fetchMovie(id: Int) {
        viewModelScope.launch {
            try {
                val deferred = async { apiHelper.loadMovie(id, LANG_ENG) }
                val movie = deferred.await()
                currentMovie.postValue(movie)
            } catch (e: Exception){
                Log.d("fetchMovie:", "fail - $e")
            }
        }
    }

    fun fetchVideos(id: Int) {
        viewModelScope.launch {
            try {
                val deferred = async { apiHelper.loadMovieDetails(id, LANG_ENG) }
                val fetchedVideos = deferred.await()
                videos.postValue(fetchedVideos.movieVideos)
            } catch (e: Exception) {
                Log.d("fetchVideos:", "fail - $e")
            }
        }
    }

    fun fetchReviews(id: Int) {
        viewModelScope.launch {
            try {
                val deferred = async {
                    apiHelper.loadMovieReviews(id, 1, LANG_ENG)
                }
                val fetchedReviews = deferred.await()
                reviews.postValue(fetchedReviews.movieReviews)
                Log.d("fetchReviews:", "success")
            } catch (e: Exception) {
                Log.d("fetchReviews:", "fail - $e")
            }
        }
    }

    fun insertMovieToDB() {
        currentMovie.value?.let {
            viewModelScope.launch {
                try {
                    databaseHelper.insertMovie(it)
                    movieInDB.postValue(true)
                    Log.d("insertMovie:", "success")
                } catch (e: Exception) {
                    Log.d("insertMovieToDB:", "fail - $e")
                }
            }
        }
    }

    fun removeMovieFromDB() {
        currentMovie.value?.id?.let{
            viewModelScope.launch {
                try {
                    databaseHelper.removeMovie(it)
                    movieInDB.postValue(false)
                    Log.d("removeMovieFromDB:", "success")
                } catch (e: Exception) {
                    Log.d("removeMovieFromDB:", "fail - $e")
                }
            }
        }
    }
}