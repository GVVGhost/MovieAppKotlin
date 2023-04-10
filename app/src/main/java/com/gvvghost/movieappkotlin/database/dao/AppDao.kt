package com.gvvghost.movieappkotlin.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import com.gvvghost.movieappkotlin.database.entity.User
import com.gvvghost.movieappkotlin.pojo.Movie

@Dao
interface AppDao {
    @Query("SELECT * FROM favorite_movies")
    suspend fun getAllFavoriteMovies(): List<Movie>

    @Query("SELECT * FROM favorite_movies WHERE id = :movieId")
    suspend fun getFavoriteMovie(movieId: Int): Movie?

    @Insert
    suspend fun insertMovie(movie: Movie)

    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    suspend fun removeMovie(movieId: Int)

    @Query("SELECT * FROM registered_users")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM registered_users WHERE email = :userName")
    suspend fun getUser(userName: String): User?

    @Insert(onConflict = IGNORE)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM registered_users WHERE email = :userEmail")
    suspend fun removeUser(userEmail: String)

    @Query("DELETE FROM registered_users")
    suspend fun removeAll()
}