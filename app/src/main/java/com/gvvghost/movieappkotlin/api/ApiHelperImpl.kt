package com.gvvghost.movieappkotlin.api

import com.gvvghost.movieappkotlin.pojo.Movie
import com.gvvghost.movieappkotlin.pojo.MovieDetailResponse
import com.gvvghost.movieappkotlin.pojo.MovieResponse
import com.gvvghost.movieappkotlin.pojo.MovieReviewResponse

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {

    override suspend fun loadMovies(
        page: Int,
        language: String,
        sortBy: String,
        voteAverage: Int,
    ): MovieResponse = apiService
        .loadMovies(
            page,
            language,
            sortBy,
            voteAverage,
            API_KEY
        )

    override suspend fun loadMovie(
        id: Int,
        language: String,
    ): Movie = apiService
        .loadMovie(
            id,
            language,
            API_KEY
        )

    override suspend fun loadMovieDetails(
        id: Int,
        language: String
    ): MovieDetailResponse = apiService
        .loadMovieDetails(
            id,
            language,
            API_KEY
        )

    override suspend fun loadMovieReviews(
        id: Int,
        page: Int,
        language: String
    ): MovieReviewResponse = apiService
        .loadMovieReviews(
            id,
            page,
            language,
            API_KEY
        )
}