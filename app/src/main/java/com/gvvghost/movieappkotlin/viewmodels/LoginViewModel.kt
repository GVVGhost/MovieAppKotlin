package com.gvvghost.movieappkotlin.viewmodels

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gvvghost.movieappkotlin.LoginActivity
import com.gvvghost.movieappkotlin.LoginActivity.Companion.EMAIL
import com.gvvghost.movieappkotlin.LoginActivity.Companion.IS_LOGGED_IN
import com.gvvghost.movieappkotlin.LoginActivity.Companion.PASSWORD
import com.gvvghost.movieappkotlin.database.AppDatabase
import com.gvvghost.movieappkotlin.database.User
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "LoginViewModel"
        private const val MIN_PASSWORD_LENGTH = 5
    }

    private var appDao = AppDatabase.getInstance(application).appDao()
    private val compositeDisposable = CompositeDisposable()
    private val error: MutableLiveData<String> = MutableLiveData<String>()
    private val user: MutableLiveData<User> = MutableLiveData<User>()
    private val sPref: SharedPreferences = application
        .getSharedPreferences(LoginActivity.MY_PREF, AppCompatActivity.MODE_PRIVATE)

    fun autologin() {
        with(sPref) {
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
        if (areUserCredentialsValid(email, password)) validateUser(
            email,
            {
                if (password == it.password) {
                    user.value = it
                    updateSharedPref(it)
                } else error.value = "Incorrect password"
            }, {
                error.value = "A login error occurred while working with the database"
                Log.d(TAG, "login: ${it.message}")
            }
        )
    }

    fun register(email: String, password: String) {
        if (areUserCredentialsValid(email, password)) validateUser(
            email,
            { error.value = "User already exist" },
            {
                with(User(email, password)) {
                    val disposable = appDao.insertUser(this)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            {
                                user.value = this
                                updateSharedPref(this)
                            },
                            {
                                error.value = "Registration failed. " +
                                        "Try again or relaunch/reinstall app"
                                Log.d(TAG, "register, failed: ${it.message}")
                            }
                        )
                    compositeDisposable.add(disposable)
                }
            }
        )
    }

    private fun validateUser(
        email: String,
        success: Consumer<User>,
        error: Consumer<Throwable>
    ) {
        val disposable = appDao.getUser(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(success, error)
        compositeDisposable.add(disposable)
    }

    private fun updateSharedPref(user: User) {
        sPref.edit()
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

    private fun isUserNameNotValid(username: String): Boolean =
        if (username.contains("@")) !Patterns.EMAIL_ADDRESS.matcher(username).matches()
        else username.trim().isEmpty()

    private fun isPasswordNotValid(password: String): Boolean =
        password.trim().length <= MIN_PASSWORD_LENGTH

    fun getUser(): LiveData<User> = user

    fun getError(): LiveData<String> = error

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}