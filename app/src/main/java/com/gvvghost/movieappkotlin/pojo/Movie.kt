package com.gvvghost.movieappkotlin.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "favorite_movies")
data class Movie(
    @PrimaryKey @SerializedName("id") @Expose val id: Int?,
    @SerializedName("title") @Expose val title: String?,
    @SerializedName("overview") @Expose val overview: String?,
    @SerializedName("release_date") @Expose val releaseDate: String?,
    @SerializedName("poster_path") @Expose val posterPath: String?,
    @SerializedName("vote_average") @Expose val voteAverage: Double?
): Serializable
