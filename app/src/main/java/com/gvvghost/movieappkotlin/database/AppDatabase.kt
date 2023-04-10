package com.gvvghost.movieappkotlin.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gvvghost.movieappkotlin.database.dao.AppDao
import com.gvvghost.movieappkotlin.database.entity.User
import com.gvvghost.movieappkotlin.pojo.Movie

@Database(entities = [User::class, Movie::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun appDao(): AppDao
}