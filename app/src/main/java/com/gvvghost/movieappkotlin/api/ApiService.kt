package com.gvvghost.movieappkotlin.api

import com.gvvghost.movieappkotlin.pojo.MovieDetailResponse
import com.gvvghost.movieappkotlin.pojo.MovieResponse
import com.gvvghost.movieappkotlin.pojo.MovieReviewResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("discover/movie")
    fun loadMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US",
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("vote_average.gte") voteAverage: Int = 4,
        @Query("api_key") apiKey: String = API_KEY
    ): Single<MovieResponse>

    @GET("movie/{movieId}/videos")
    fun loadMovieDetails(
        @Path("movieId") id: Int,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String = API_KEY
    ): Single<MovieDetailResponse>

    @GET("movie/{movieId}/reviews")
    fun loadMovieReviews(
        @Path("movieId") id: Int,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String = API_KEY
    ): Single<MovieReviewResponse>
}