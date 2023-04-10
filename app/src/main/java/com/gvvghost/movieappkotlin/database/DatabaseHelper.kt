package com.gvvghost.movieappkotlin.database

import com.gvvghost.movieappkotlin.database.entity.User
import com.gvvghost.movieappkotlin.pojo.Movie

interface DatabaseHelper {

    suspend fun getAllFavoriteMovies(): List<Movie>

    suspend fun getFavoriteMovie(movieId: Int): Movie?

    suspend fun insertMovie(movie: Movie)

    suspend fun removeMovie(movieId: Int)

    suspend fun getAllUsers(): List<User>

    suspend fun getUser(userName: String): User?

    suspend fun insertUser(user: User)

    suspend fun removeUser(userEmail: String)

    suspend fun removeAll()
}