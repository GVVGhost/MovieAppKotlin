package com.gvvghost.movieappkotlin.database

import androidx.lifecycle.LiveData
import com.gvvghost.movieappkotlin.database.entity.User
import com.gvvghost.movieappkotlin.pojo.Movie

class DatabaseHelperImpl(
    private val appDatabase: AppDatabase
) : DatabaseHelper {

    override suspend fun getAllFavoriteMovies(): List<Movie> =
        appDatabase.appDao().getAllFavoriteMovies()

    override suspend fun getFavoriteMovie(movieId: Int): Movie? =
        appDatabase.appDao().getFavoriteMovie(movieId)

    override suspend fun insertMovie(movie: Movie) =
        appDatabase.appDao().insertMovie(movie)

    override suspend fun removeMovie(movieId: Int) =
        appDatabase.appDao().removeMovie(movieId)

    override suspend fun getAllUsers(): List<User> =
        appDatabase.appDao().getAllUsers()

    override suspend fun getUser(userName: String): User? =
        appDatabase.appDao().getUser(userName)

    override suspend fun insertUser(user: User) =
        appDatabase.appDao().insertUser(user)

    override suspend fun removeUser(userEmail: String) =
        appDatabase.appDao().removeUser(userEmail)

    override suspend fun removeAll() =
        appDatabase.appDao().removeAll()
}