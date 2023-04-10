package com.gvvghost.movieappkotlin.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gvvghost.movieappkotlin.api.ApiHelper
import com.gvvghost.movieappkotlin.database.DatabaseHelper
import com.gvvghost.movieappkotlin.database.entity.User
import com.gvvghost.movieappkotlin.util.Constants.EMAIL
import com.gvvghost.movieappkotlin.util.Constants.IS_LOGGED_IN
import com.gvvghost.movieappkotlin.util.Constants.PASSWORD
import com.gvvghost.movieappkotlin.util.Credentials.isPasswordNotValid
import com.gvvghost.movieappkotlin.util.Credentials.isUserNameNotValid
import kotlinx.coroutines.launch

class LoginViewModel(
    private val apiHelper: ApiHelper,
    private val databaseHelper: DatabaseHelper,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val error: MutableLiveData<String> = MutableLiveData<String>()
    private val user: MutableLiveData<User> = MutableLiveData<User>()
    fun getError(): LiveData<String> = error
    fun getUser(): LiveData<User> = user

    fun autologin() {
        with(sharedPreferences) {
            if (contains(IS_LOGGED_IN)
                && getBoolean(IS_LOGGED_IN, false)
                && contains(EMAIL)
                && contains(PASSWORD)
            ) {
                val email = getString(EMAIL, "")
                val password = getString(PASSWORD, "")
                if (email != null && password != null) login(email, password)
            }
        }
    }

    fun login(email: String, password: String) {
        if (areUserCredentialsValid(email, password)) {
            viewModelScope.launch {
                try {
                    val fetchedUser = databaseHelper.getUser(email)
                    fetchedUser?.let {
                        if (password == it.password) {
                            user.postValue(it)
                            updateSharedPref(it)
                        } else error.postValue("Incorrect password")
                    } ?: run { error.postValue("User not found") }
                } catch (e: Exception) {
                    error.value = "A login error occurred while working with the local database"
                    Log.d("Login", e.toString())
                }
            }
        }
    }

    fun register(email: String, password: String) {
        if (areUserCredentialsValid(email, password)) {
            viewModelScope.launch {
                try {
                    val fetchedUser = databaseHelper.getUser(email)
                    fetchedUser?.let {
                        error.postValue("User already exists")
                    } ?: run {
                        val newUser = User(email, password)
                        databaseHelper.insertUser(newUser)
                        updateSharedPref(newUser)
                    }
                } catch (e: java.lang.Exception) {
                    error.value = "Registration failed. Try again or relaunch/reinstall app"
                    Log.d("Register", e.toString())
                }
            }
        }
    }

    private fun updateSharedPref(user: User) {
        sharedPreferences.edit()
            .putString(PASSWORD, user.password)
            .putBoolean(IS_LOGGED_IN, true)
            .putString(EMAIL, user.email)
            .apply()
    }

    private fun areUserCredentialsValid(email: String, password: String): Boolean =
        if (isUserNameNotValid(email)) {
            error.value = "Invalid username"
            false
        } else if (isPasswordNotValid(password)) {
            error.value = "Invalid password"
            false
        } else true
}