package com.gvvghost.movieappkotlin.api

import com.gvvghost.movieappkotlin.pojo.Movie
import com.gvvghost.movieappkotlin.pojo.MovieDetailResponse
import com.gvvghost.movieappkotlin.pojo.MovieResponse
import com.gvvghost.movieappkotlin.pojo.MovieReviewResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("discover/movie")
    suspend fun loadMovies(
        @Query("page") page: Int,
        @Query("language") language: String,
        @Query("sort_by") sortBy: String,
        @Query("vote_average.gte") voteAverage: Int,
        @Query("api_key") apiKey: String
    ): MovieResponse

    @GET("movie/{movieId}")
    suspend fun loadMovie(
        @Path("movieId") id: Int,
        @Query("language") language: String,
        @Query("api_key") apiKey: String
    ): Movie

    @GET("movie/{movieId}/videos")
    suspend fun loadMovieDetails(
        @Path("movieId") id: Int,
        @Query("language") language: String,
        @Query("api_key") apiKey: String
    ): MovieDetailResponse

    @GET("movie/{movieId}/reviews")
    suspend fun loadMovieReviews(
        @Path("movieId") id: Int,
        @Query("page") page: Int,
        @Query("language") language: String,
        @Query("api_key") apiKey: String
    ): MovieReviewResponse
}