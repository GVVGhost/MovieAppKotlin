package com.gvvghost.movieappkotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gvvghost.movieappkotlin.api.ApiHelperImpl
import com.gvvghost.movieappkotlin.api.RetrofitBuilder
import com.gvvghost.movieappkotlin.database.DatabaseBuilder
import com.gvvghost.movieappkotlin.database.DatabaseHelperImpl
import com.gvvghost.movieappkotlin.databinding.ActivityRegistrationBinding
import com.gvvghost.movieappkotlin.util.Constants
import com.gvvghost.movieappkotlin.util.Constants.EMAIL
import com.gvvghost.movieappkotlin.viewmodels.LoginViewModel
import com.gvvghost.movieappkotlin.viewmodels.factory.ViewModelFactory

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    companion object {
        fun newIntent(context: Context, email: String): Intent {
            val intent = Intent(context, RegistrationActivity::class.java)
            intent.putExtra(EMAIL, email)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel = ViewModelProvider(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
                application.getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE)
            )
        )[LoginViewModel::class.java]
        intent.getStringExtra(EMAIL)?.run { binding.editTextEmail.setText(this) }
        binding.buttonRegister.setOnClickListener {
            viewModel.register(
                binding.editTextEmail.text.toString(),
                binding.editTextPassword.text.toString()
            )
        }
        viewModel.getError().observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        viewModel.getUser().observe(this) { finish() }
    }
}