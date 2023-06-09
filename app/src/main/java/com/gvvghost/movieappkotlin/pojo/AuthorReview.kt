package com.gvvghost.movieappkotlin.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AuthorReview(
    @SerializedName("author") @Expose val author: String?,
    @SerializedName("content") @Expose val content: String?,
    @SerializedName("author_details") @Expose val authorDetail: AuthorDetails?
)
