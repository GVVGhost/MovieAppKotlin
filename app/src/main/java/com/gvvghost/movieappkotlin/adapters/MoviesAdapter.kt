package com.gvvghost.movieappkotlin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gvvghost.movieappkotlin.R
import com.gvvghost.movieappkotlin.R.drawable.*
import com.gvvghost.movieappkotlin.pojo.Movie
import com.gvvghost.movieappkotlin.util.Constants.BASE_URL_IMAGE
import com.gvvghost.movieappkotlin.util.Constants.POSTER_SIZE_W154
import java.io.Serializable

class MoviesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onReachEndListener: OnReachEndListener? = null
    var onMovieClickListener: OnMovieClickListener? = null
    var layoutType = LayoutType.GRID
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var movies: List<Movie> = ArrayList()
        @SuppressLint("NotifyDataSetChanged")
        set(list) {
            field = list
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (layoutType == LayoutType.GRID)
            GridViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.movie_card_item, parent, false)
            )
        else
            ListViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.movie_list_item, parent, false)
            )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = movies[position]
        val rating: Double = movie.voteAverage ?: run { 0.0 }
        var difForPreload = 6
        if (layoutType == LayoutType.GRID && holder is GridViewHolder) {
            holder.apply {
                Glide.with(itemView)
                    .load(BASE_URL_IMAGE + POSTER_SIZE_W154 + movie.posterPath)
                    .into(posterIv)
                titleTv.text = movie.title
                ratingTv.text = movie.voteAverage.toString()
                ratingTv.background = ContextCompat.getDrawable(
                    itemView.context,
                    if (rating > 8) rating_bg_green
                    else if (rating > 6) rating_bg_orange
                    else rating_bg_red
                )
            }
        } else if (layoutType == LayoutType.LIST && holder is ListViewHolder) {
            difForPreload = 2
            holder.apply {
                Glide.with(itemView)
                    .load(BASE_URL_IMAGE + POSTER_SIZE_W154 + movie.posterPath)
                    .into(posterIv)
                titleTv.text = movie.title
                ratingTv.text = movie.voteAverage.toString()
                contentTv.text = movie.overview
                releaseDateTv.text = movie.releaseDate
            }
        }
        onReachEndListener?.apply { if (position >= (movies.size - difForPreload)) onReachEnd() }
        onMovieClickListener?.apply {
            holder.itemView.setOnClickListener { this.onMovieClick(movie) }
        }
    }

    override fun getItemCount(): Int = movies.size

    // TODO There is another way to write a ViewHolder
    class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val posterIv: ImageView = itemView.findViewById(R.id.imageViewPosterGrid)
        val titleTv: TextView = itemView.findViewById(R.id.textViewMovieTitleGrid)
        val ratingTv: TextView = itemView.findViewById(R.id.textViewRatingGrid)
    }

    // TODO There is another way to write a ViewHolder
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val posterIv: ImageView = itemView.findViewById(R.id.imageViewPosterList)
        val titleTv: TextView = itemView.findViewById(R.id.textViewMovieTitleList)
        val releaseDateTv: TextView = itemView.findViewById(R.id.textViewReleaseDateList)
        val contentTv: TextView = itemView.findViewById(R.id.textViewContentList)
        val ratingTv: TextView = itemView.findViewById(R.id.textViewRatingList)
    }

    // TODO `:Serializable` could be removed (required to checking out)
    enum class LayoutType(val layout: String) : Serializable {
        GRID("GRID"), LIST("LIST");
    }

    interface OnReachEndListener {
        fun onReachEnd()
    }

    interface OnMovieClickListener {
        fun onMovieClick(movie: Movie)
    }
}