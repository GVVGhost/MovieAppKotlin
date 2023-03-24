package com.gvvghost.movieappkotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gvvghost.movieappkotlin.LoginActivity.Companion.EMAIL
import com.gvvghost.movieappkotlin.databinding.ActivityRegistrationBinding
import com.gvvghost.movieappkotlin.viewmodels.LoginViewModel


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
        val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
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