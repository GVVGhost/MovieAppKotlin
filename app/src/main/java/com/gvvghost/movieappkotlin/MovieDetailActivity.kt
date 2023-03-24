package com.gvvghost.movieappkotlin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.gvvghost.movieappkotlin.R.drawable.ic_bookmark_chkd
import com.gvvghost.movieappkotlin.R.drawable.ic_bookmark_unchkd
import com.gvvghost.movieappkotlin.adapters.ReviewAdapter
import com.gvvghost.movieappkotlin.adapters.VideoAdapter
import com.gvvghost.movieappkotlin.databinding.ActivityMovieDetailBinding
import com.gvvghost.movieappkotlin.pojo.Movie
import com.gvvghost.movieappkotlin.pojo.MovieVideo
import com.gvvghost.movieappkotlin.viewmodels.MovieDetailViewModel

class MovieDetailActivity : AppCompatActivity() {

    companion object {
        const val BASE_URL_YOUTUBE = "https://www.youtube.com/watch?v="
        private const val POSTER_BASE_URL = "http://image.tmdb.org/t/p/w342"
        private const val TAG = "MovieDetailActivity"
        private const val EXTRA_MOVIE = "movie"
        fun newIntent(context: Context, movie: Movie): Intent {
            val intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra(EXTRA_MOVIE, movie)
            return intent
        }
    }

    private lateinit var binding: ActivityMovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val movie =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.getSerializableExtra(EXTRA_MOVIE, Movie::class.java)
            else intent.getSerializableExtra(EXTRA_MOVIE) as Movie

        val viewModel = ViewModelProvider(this)[MovieDetailViewModel::class.java]
        val videoAdapter = VideoAdapter()
        val reviewAdapter = ReviewAdapter()
        binding.recycleViewVideosDetail.adapter = videoAdapter
        binding.recycleViewReviewsDetail.adapter = reviewAdapter
        movie?.apply {
            binding.textViewMovieTitleDetail.text = title
            binding.textViewReleaseDateDetail.text = releaseDate
            binding.textViewContentDetail.text = overview
            Glide.with(this@MovieDetailActivity)
                .load(POSTER_BASE_URL + posterPath)
                .into(binding.imageViewPosterDetail)
        }
        viewModel.getVideos().observe(this) { videoAdapter.videos = it }
        viewModel.getReviews().observe(this) { reviewAdapter.reviews = it }
        movie?.id?.apply {
            viewModel.getFavoriteMovies(this).observe(this@MovieDetailActivity) {
                //TODO check if 'it: Movie' can be null? It probably should be null if no entry of it does not exist in DB
                if (it == null) {
                    Log.d(TAG, "getFavoriteMovies: movie is null")
                    binding.imageViewFavoriteDetail.setOnClickListener { viewModel.insertMovie(movie) }
                    binding.imageViewFavoriteDetail.setImageDrawable(
                        ContextCompat.getDrawable(this@MovieDetailActivity, ic_bookmark_unchkd)
                    )
                } else {
                    Log.d(TAG, "getFavoriteMovies: movie isn`t null")
                    binding.imageViewFavoriteDetail.setOnClickListener { viewModel.removeMovie(movie.id) }
                    binding.imageViewFavoriteDetail.setImageDrawable(
                        ContextCompat.getDrawable(this@MovieDetailActivity, ic_bookmark_chkd)
                    )
                }
            }
        }
        viewModel.getError().observe(this){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        videoAdapter.onVideoClickListener = object : VideoAdapter.OnVideoClickListener{
            override fun onVideoClick(movieVideo: MovieVideo) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(BASE_URL_YOUTUBE + movieVideo.key)
                startActivity(intent)
            }
        }
        movie?.id?.apply {
            viewModel.loadVideos(this)
            viewModel.loadReviews(this)
        }
    }
}