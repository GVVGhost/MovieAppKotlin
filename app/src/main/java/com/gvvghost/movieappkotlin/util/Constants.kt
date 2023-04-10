package com.gvvghost.movieappkotlin.util

object Constants {
    //Constants for The Movie Data base API
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val BASE_URL_IMAGE = "http://image.tmdb.org/t/p/"
    const val POSTER_SIZE_W92 = "w92"
    const val POSTER_SIZE_W154 = "w154"
    const val POSTER_SIZE_W185 = "w185"
    const val POSTER_SIZE_W324 = "w342"
    const val POSTER_SIZE_W500 = "w500"
    const val POSTER_SIZE_W780 = "w780"
    const val POSTER_SIZE_ORIGINAL = "original"
    const val BASE_URL_YOUTUBE = "https://www.youtube.com/"
    const val YOUTUBE_WATCH = "watch?v="

    //Constance for activities and viewModels
    const val MY_PREF = "mypref"
    const val IS_LOGGED_IN = "isLoggedIn"
    const val EMAIL = "email"
    const val PASSWORD = "password"
    const val EXTRA_MOVIE_ID = "movieId"
    const val MIN_PASSWORD_LENGTH = 5

    //Constants for Retrofit requests
    const val LANG_ENG = "en-US"
    const val SORT_DESC = "popularity.desc"
}