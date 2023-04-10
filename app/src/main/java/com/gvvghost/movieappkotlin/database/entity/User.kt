package com.gvvghost.movieappkotlin.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "registered_users")
data class User(@PrimaryKey val email: String, val password: String) : Serializable