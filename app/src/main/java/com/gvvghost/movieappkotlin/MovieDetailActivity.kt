package com.gvvghost.movieappkotlin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getDrawable
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.gvvghost.movieappkotlin.R.drawable.ic_bookmark_chkd
import com.gvvghost.movieappkotlin.R.drawable.ic_bookmark_unchkd
import com.gvvghost.movieappkotlin.adapters.ReviewAdapter
import com.gvvghost.movieappkotlin.adapters.VideoAdapter
import com.gvvghost.movieappkotlin.api.ApiHelperImpl
import com.gvvghost.movieappkotlin.api.RetrofitBuilder
import com.gvvghost.movieappkotlin.database.DatabaseBuilder
import com.gvvghost.movieappkotlin.database.DatabaseHelperImpl
import com.gvvghost.movieappkotlin.databinding.ActivityMovieDetailBinding
import com.gvvghost.movieappkotlin.pojo.MovieVideo
import com.gvvghost.movieappkotlin.util.Constants.BASE_URL_IMAGE
import com.gvvghost.movieappkotlin.util.Constants.BASE_URL_YOUTUBE
import com.gvvghost.movieappkotlin.util.Constants.EXTRA_MOVIE_ID
import com.gvvghost.movieappkotlin.util.Constants.MY_PREF
import com.gvvghost.movieappkotlin.util.Constants.POSTER_SIZE_W324
import com.gvvghost.movieappkotlin.util.Constants.YOUTUBE_WATCH
import com.gvvghost.movieappkotlin.util.UIMessages.showToast
import com.gvvghost.movieappkotlin.viewmodels.MovieDetailViewModel
import com.gvvghost.movieappkotlin.viewmodels.factory.ViewModelFactory

class MovieDetailActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context, movieId: Int): Intent {
            val intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra(EXTRA_MOVIE_ID, movieId)
            return intent
        }
    }

    private lateinit var binding: ActivityMovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1)
        val viewModel = ViewModelProvider(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
                application.getSharedPreferences(MY_PREF, MODE_PRIVATE)
            )
        )[MovieDetailViewModel::class.java]

        val videoAdapter = VideoAdapter()
        val reviewAdapter = ReviewAdapter()
        binding.recycleViewVideosDetail.adapter = videoAdapter
        binding.recycleViewReviewsDetail.adapter = reviewAdapter

        viewModel.getMovie().observe(this) {
            it?.apply {
                binding.textViewMovieTitleDetail.text = title
                binding.textViewReleaseDateDetail.text = releaseDate
                binding.textViewContentDetail.text = overview
                Glide.with(this@MovieDetailActivity)
                    .load(BASE_URL_IMAGE + POSTER_SIZE_W324 + posterPath)
                    .into(binding.imageViewPosterDetail)
                id?.let { id ->
                    viewModel.fetchVideos(id)
                    viewModel.fetchReviews(id)
                }
                binding.imageViewFavoriteDetail.setOnClickListener { viewModel.insertMovieToDB() }
            } ?: run { showToast(this, "Failed to download movie data") }
        }
        viewModel.getVideos().observe(this) { videoAdapter.videos = it }
        viewModel.getReviews().observe(this) { reviewAdapter.reviews = it }
        viewModel.getError().observe(this) { showToast(this, it) }
        viewModel.isMovieInDB().observe(this) {
            if (it) binding.imageViewFavoriteDetail.apply {
                setImageDrawable(getDrawable(this@MovieDetailActivity, ic_bookmark_chkd))
                setOnClickListener { viewModel.removeMovieFromDB() }
            } else binding.imageViewFavoriteDetail.apply {
                setImageDrawable(getDrawable(this@MovieDetailActivity, ic_bookmark_unchkd))
                setOnClickListener { viewModel.insertMovieToDB() }
            }
        }
        videoAdapter.onVideoClickListener = object : VideoAdapter.OnVideoClickListener {
            override fun onVideoClick(movieVideo: MovieVideo) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(BASE_URL_YOUTUBE + YOUTUBE_WATCH + movieVideo.key)
                startActivity(intent)
            }
        }
        viewModel.fetchMovie(movieId)
    }
}