package com.gvvghost.movieappkotlin.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(
    @SerializedName("id") @Expose val id: Int?,
    @SerializedName("results") @Expose val movieVideos: List<MovieVideo>?
)


