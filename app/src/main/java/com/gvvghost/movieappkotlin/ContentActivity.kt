package com.gvvghost.movieappkotlin

import android.content.Context
import android.content.Intent
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.gvvghost.movieappkotlin.R.string.nav_close
import com.gvvghost.movieappkotlin.R.string.nav_open
import com.gvvghost.movieappkotlin.adapters.MoviesAdapter
import com.gvvghost.movieappkotlin.adapters.MoviesAdapter.LayoutType
import com.gvvghost.movieappkotlin.adapters.MoviesAdapter.LayoutType.GRID
import com.gvvghost.movieappkotlin.adapters.MoviesAdapter.LayoutType.LIST
import com.gvvghost.movieappkotlin.databinding.ActivityContentBinding
import com.gvvghost.movieappkotlin.pojo.Movie
import com.gvvghost.movieappkotlin.viewmodels.ContentViewModel

class ContentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContentBinding
    private lateinit var viewModel: ContentViewModel
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private val layoutType = MutableLiveData<LayoutType>()
    private var isSetOnMarked = false

    companion object {
        private const val TAG = "ContentActivity"
        private const val VIEW_TYPE_PARAM = "viewType"
        fun newIntent(context: Context, email: String): Intent {
            val intent = Intent(context, ContentActivity::class.java)
            intent.putExtra(LoginActivity.EMAIL, email)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.run {
            val lt = this.getString(VIEW_TYPE_PARAM)
            layoutType.value = if (LIST.layout == lt) LIST else GRID
        } ?: run { layoutType.value = GRID }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val gridLM = GridLayoutManager(this, if (isOrientationLandscape()) 5 else 3)
        val linearLM = LinearLayoutManager(this)
        val drawerLayout = binding.contentDrawerLayout
        val adapter = MoviesAdapter()
        layoutType.value?.run { adapter.layoutType = this } ?: run { adapter.layoutType = GRID }
        binding.recycleViewMovies.apply {
            setHasFixedSize(true)
            this.adapter = adapter
            layoutManager = if (layoutType.value == GRID) gridLM else linearLM
        }

        viewModel = ViewModelProvider(this)[ContentViewModel::class.java]
        viewModel.getMovies().observe(this) { adapter.movies = it }
        viewModel.getIsLoading().observe(this) { isLoading ->
            binding.progressBarLoading.visibility = if (isLoading) VISIBLE else GONE
        }
        layoutType.observe(this) { setLayoutType ->
            setLayoutType?.run { adapter.layoutType = this } ?: run { adapter.layoutType = GRID }
            binding.recycleViewMovies.apply {
                layoutManager = if (setLayoutType == GRID) gridLM else linearLM
                this.adapter = adapter
            }
        }
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, nav_open, nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        adapter.onReachEndListener = object : MoviesAdapter.OnReachEndListener {
            override fun onReachEnd() = viewModel.loadMovies()
        }
        adapter.onMovieClickListener = object : MoviesAdapter.OnMovieClickListener {
            override fun onMovieClick(movie: Movie) =
                startActivity(MovieDetailActivity.newIntent(this@ContentActivity, movie))
        }

        binding.navigationMenu.setNavigationItemSelectedListener {
            var result = false
            when (it.itemId) {
                R.id.movies_marked -> {
                    if (!isSetOnMarked) {
                        isSetOnMarked = true
                        viewModel.loadMarkedMovies()
                        adapter.onReachEndListener = null
                    }
                    result = true
                }
                R.id.movies_all -> {
                    if (isSetOnMarked) {
                        isSetOnMarked = false
                        viewModel.loadMovies(false)
                        adapter.onReachEndListener = object : MoviesAdapter.OnReachEndListener {
                            override fun onReachEnd() = viewModel.loadMovies()
                        }
                    }
                    result = true
                }
                R.id.user_profile -> {
                    val username = intent.getStringExtra(LoginActivity.EMAIL)
                    username?.also {
                        startActivity(
                            UserProfileActivity.newIntent(
                                this@ContentActivity,
                                username
                            )
                        )
                    } ?: run {
                        Log.d(TAG, "intent extra username is null")
                        Toast.makeText(
                            this@ContentActivity,
                            "Launch of user profile activity failed, username is null",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    result = true
                }
                R.id.nav_logout -> {
                    viewModel.logout()
                    startActivity(LoginActivity.newIntent(this@ContentActivity))
                    finish()
                    result = true
                }
            }
            result
        }
        val connectivityManager: ConnectivityManager = getSystemService()!!
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.simple_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_item_list_view && layoutType.value != LIST)
            layoutType.value = LIST
        if (item.itemId == R.id.menu_item_table_view && layoutType.value != GRID)
            layoutType.value = GRID
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) true
        else super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        layoutType.value?.run {
            val line = this.layout
            outState.putString(VIEW_TYPE_PARAM, line)
        }
    }

    private fun isOrientationLandscape(): Boolean =
        resources.configuration.orientation == ORIENTATION_LANDSCAPE

    private fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        if (message.isNotBlank()) Toast.makeText(this, message, duration).show()
    }

    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    private val networkCallback = object
        : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
//            showToast("Internet connection was established")
            if (!isSetOnMarked && viewModel.isMovieListEmpty()) viewModel.loadMovies(false)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            showToast("Internet connection was lost")
        }
    }
}