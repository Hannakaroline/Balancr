package com.hanna.balancr.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.hanna.balancr.model.BalancrDataBase
import com.hanna.balancr.model.entities.User
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.*

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    enum class AuthenticationState {
        UnAuthenticated,
        Authenticated,
        InvalidAuthentication
    }

    private val messageDigest = MessageDigest.getInstance("SHA-256")
    private val database = BalancrDataBase.getInstance(application.applicationContext)

    val authenticationState = MutableLiveData(AuthenticationState.UnAuthenticated)

    init {
        viewModelScope.launch {
            authenticationState.value = if (database.userDao().currentUser() == null) {
                AuthenticationState.UnAuthenticated
            } else {
                AuthenticationState.Authenticated
            }
        }
    }

    fun authenticate(email: String, password: String) {
        viewModelScope.launch {
            val user = findUserFor(email, password)
            if (user != null) {
                val allUsers = database.userDao().getAllUsers()
                database.userDao().updateUsers(allUsers.map { it.copy(isLoggedIn = false) })
                database.userDao().updateUser(user.copy(isLoggedIn = true))
                authenticationState.value = AuthenticationState.Authenticated
            } else {
                authenticationState.value = AuthenticationState.InvalidAuthentication
            }
        }
    }

    private suspend fun findUserFor(email: String, password: String): User? {
        val user = database.userDao().getUserByEmail(email)
        val digestedPassword = messageDigest.digest(password.toByteArray())
        if (user != null && user.password == Arrays.toString(digestedPassword))
            return user
        return null
    }

    fun logout() {
        viewModelScope.launch {
            val currentUser = database.userDao().currentUser()
            currentUser?.let { user ->
                database.userDao().updateUser(user.copy(isLoggedIn = false))
            }
            authenticationState.value = AuthenticationState.UnAuthenticated
        }
    }

    fun refuseAuthentication() {
        this.authenticationState.value = AuthenticationState.UnAuthenticated
    }
}