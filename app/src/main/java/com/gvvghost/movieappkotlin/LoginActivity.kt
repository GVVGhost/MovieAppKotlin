package com.gvvghost.movieappkotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gvvghost.movieappkotlin.api.ApiHelperImpl
import com.gvvghost.movieappkotlin.api.RetrofitBuilder
import com.gvvghost.movieappkotlin.database.DatabaseBuilder
import com.gvvghost.movieappkotlin.database.DatabaseHelperImpl
import com.gvvghost.movieappkotlin.databinding.ActivityLoginBinding
import com.gvvghost.movieappkotlin.util.Constants.MY_PREF
import com.gvvghost.movieappkotlin.util.UIMessages.showToast
import com.gvvghost.movieappkotlin.viewmodels.LoginViewModel
import com.gvvghost.movieappkotlin.viewmodels.factory.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
                application.getSharedPreferences(MY_PREF, MODE_PRIVATE)
            )
        )[LoginViewModel::class.java]

        binding.buttonLogin.setOnClickListener {
            viewModel.login(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }
        binding.buttonRegister.setOnClickListener {
            startActivity(
                RegistrationActivity.newIntent(this, binding.etEmail.text.toString())
            )
        }
        viewModel.getError().observe(this) { showToast(this@LoginActivity, it) }
        viewModel.getUser().observe(this) {
            startActivity(ContentActivity.newIntent(this, it.email))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.autologin()
    }
}