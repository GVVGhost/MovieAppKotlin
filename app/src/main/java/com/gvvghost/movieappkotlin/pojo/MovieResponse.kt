package com.gvvghost.movieappkotlin.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieResponse(@SerializedName("results") @Expose val movieList: List<Movie>? = null)