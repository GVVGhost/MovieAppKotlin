package com.gvvghost.movieappkotlin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gvvghost.movieappkotlin.LoginActivity.Companion.EMAIL
import com.gvvghost.movieappkotlin.databinding.ActivityUserProfileBinding

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