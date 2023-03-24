package com.gvvghost.movieappkotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gvvghost.movieappkotlin.databinding.ActivityLoginBinding
import com.gvvghost.movieappkotlin.viewmodels.LoginViewModel

class LoginActivity : AppCompatActivity() {

    companion object {
        const val MY_PREF = "mypref"
        const val IS_LOGGED_IN = "isLoggedIn"
        const val EMAIL = "email"
        const val PASSWORD = "password"
        fun newIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
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
        viewModel.getError().observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
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