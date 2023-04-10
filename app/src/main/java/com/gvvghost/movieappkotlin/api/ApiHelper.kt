package com.gvvghost.movieappkotlin.api

import com.gvvghost.movieappkotlin.pojo.Movie
import com.gvvghost.movieappkotlin.pojo.MovieDetailResponse
import com.gvvghost.movieappkotlin.pojo.MovieResponse
import com.gvvghost.movieappkotlin.pojo.MovieReviewResponse

interface ApiHelper {

    suspend fun loadMovies(
        page: Int,
        language: String,
        sortBy: String,
        voteAverage: Int
    ): MovieResponse

    suspend fun loadMovie(
        id: Int,
        language: String
    ): Movie

    suspend fun loadMovieDetails(
        id: Int,
        language: String
    ): MovieDetailResponse

    suspend fun loadMovieReviews(
        id: Int,
        page: Int,
        language: String
    ): MovieReviewResponse
}