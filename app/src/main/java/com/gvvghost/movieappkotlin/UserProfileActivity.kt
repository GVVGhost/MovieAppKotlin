package com.gvvghost.movieappkotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gvvghost.movieappkotlin.databinding.ActivityUserProfileBinding
import com.gvvghost.movieappkotlin.util.Constants.EMAIL

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    companion object {
        fun newIntent(context: Context, email: String): Intent {
            val intent = Intent(context, UserProfileActivity::class.java)
            intent.putExtra(EMAIL, email)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.username.text = intent.getStringExtra(EMAIL)
    }
}