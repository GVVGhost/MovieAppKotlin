package com.gvvghost.movieappkotlin.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AuthorDetails(@SerializedName("rating") @Expose val rating: Int? = null)
