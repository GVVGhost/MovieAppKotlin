package com.gvvghost.movieappkotlin.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import com.gvvghost.movieappkotlin.pojo.Movie
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface AppDao {
    @Query("SELECT * FROM favorite_movies")
    fun getAllFavoriteMovies(): Single<List<Movie>>

    @Query("SELECT * FROM favorite_movies WHERE id = :movieId")
    fun getFavoriteMovie(movieId: Int): LiveData<Movie>

    @Insert
    fun insertMovie(movie: Movie): Completable

    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    fun removeMovie(movieId: Int): Completable

    @Query("SELECT * FROM registered_users")
    fun getAllUsers(): Single<List<User>>

    @Query("SELECT * FROM registered_users WHERE email = :userName")
    fun getUser(userName: String): Single<User>

    @Insert(onConflict = IGNORE)
    fun insertUser(user: User): Completable

    @Query("DELETE FROM registered_users WHERE email = :userEmail")
    fun removeUser(userEmail: String): Completable

    @Query("DELETE FROM registered_users")
    fun removeAll(): Completable
}