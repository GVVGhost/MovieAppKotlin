package com.gvvghost.movieappkotlin.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieReviewResponse(
    @SerializedName("id") @Expose val id: Int? = null,
    @SerializedName("results") @Expose val movieReviews: MutableList<AuthorReview>? = null
)
