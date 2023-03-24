package com.gvvghost.movieappkotlin.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
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


    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}