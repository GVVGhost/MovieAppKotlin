package com.gvvghost.movieappkotlin.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieVideo(
    @SerializedName("name") @Expose val name: String?,
    @SerializedName("key") @Expose val key: String?
)
