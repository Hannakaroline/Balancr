package com.hanna.balancr.ui.sign_up

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hanna.balancr.model.BalancrDataBase
import com.hanna.balancr.model.entities.User
import com.hanna.balancr.ui.profile.ProfileViewModel
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.*
import java.util.regex.Pattern

class SignUpViewModel(application: Application) : AndroidViewModel(application) {

    private val database = BalancrDataBase.getInstance(application.applicationContext)
    val md = MessageDigest.getInstance("SHA-256")

    enum class SignUpState {
        CollectingData,
        Completed,
        InvalidInput,
        InvalidEmail,
        EmailAlreadyTaken,
        NewPasswordsDontMatch
    }

    val signUpState = MutableLiveData(SignUpState.CollectingData)

    fun createAccount(
        name: String?,
        height: Float?,
        email: String?,
        password: String?,
        passwordConfirmation: String?
    ) {
        if (name.isNullOrBlank() ||
            height == null ||
            height <= 0.0 ||
            email.isNullOrBlank() ||
            password.isNullOrBlank() ||
            passwordConfirmation.isNullOrBlank()
        ) {
            signUpState.value = SignUpState.InvalidInput
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpState.value = SignUpState.InvalidEmail
            return
        }

        if (password != passwordConfirmation) {
            signUpState.value = SignUpState.NewPasswordsDontMatch
            return
        }

        viewModelScope.launch {
            val digest = md.digest(password.toByteArray())
            val user = User(
                email = email,
                password = Arrays.toString(digest),
                isLoggedIn = false,
                name = name,
                height = height
            )

            try {
                database.userDao().insert(user)
                signUpState.value = SignUpState.Completed
            } catch (ex: SQLiteConstraintException) {
                signUpState.value = SignUpState.EmailAlreadyTaken
            }
        }
    }

    fun clearSignUpState() {
        signUpState.value = SignUpState.CollectingData
    }

}