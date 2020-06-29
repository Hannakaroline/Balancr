package com.hanna.balancr.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hanna.balancr.model.BalancrDataBase
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    enum class AuthenticationState {
        UnAuthenticated,
        Authenticated,
        InvalidAuthentication
    }

    private val dataBase = BalancrDataBase.getInstance(application.applicationContext)

    val authenticationState = MutableLiveData<AuthenticationState>()

    init {
        authenticationState.value = AuthenticationState.UnAuthenticated
    }

    fun authenticate(email: String, password: String) {
        viewModelScope.launch {
            if (passwordIsValidForUsername(email, password)) {
                authenticationState.value = AuthenticationState.Authenticated

            } else {
                authenticationState.value = AuthenticationState.InvalidAuthentication
            }
        }
    }

    private suspend fun passwordIsValidForUsername(email: String, password: String): Boolean {
        val user = dataBase.userDao().getUserByEmail(email)
        return user != null
    }

    fun logout() {
        this.authenticationState.value = AuthenticationState.UnAuthenticated
    }

    fun refuseAuthentication() {
        this.authenticationState.value = AuthenticationState.UnAuthenticated

    }
}