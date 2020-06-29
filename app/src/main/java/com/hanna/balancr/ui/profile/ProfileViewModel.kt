package com.hanna.balancr.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hanna.balancr.model.BalancrDataBase
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.*

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    enum class UpdateProfileState {
        None, InvalidInput, IncorrectPassword, NewPasswordsDontMatch, PasswordUpdateSuccess, DetailsUpdateSuccess
    }

    private val messageDigest = MessageDigest.getInstance("SHA-256")
    private val database = BalancrDataBase.getInstance(application.applicationContext)
    val currentUser = database.userDao().liveCurrentUser()
    val updatePasswordState = MutableLiveData(UpdateProfileState.None)

    fun clearUpdatePasswordState() {
        updatePasswordState.value = UpdateProfileState.None
    }

    fun updatePasswordWith(
        currentPassword: String?,
        newPassword: String?,
        passwordConfirmation: String?
    ) {
        if (currentPassword.isNullOrBlank() || newPassword.isNullOrBlank() || passwordConfirmation.isNullOrBlank()) {
            updatePasswordState.value = UpdateProfileState.InvalidInput
            return
        }

        if (newPassword != passwordConfirmation) {
            updatePasswordState.value = UpdateProfileState.NewPasswordsDontMatch
            return
        }

        val user = currentUser.value ?: return
        val passwordDigest = messageDigest.digest(currentPassword.toByteArray())

        if (user.password != Arrays.toString(passwordDigest)) {
            updatePasswordState.value = UpdateProfileState.IncorrectPassword
            return
        }

        viewModelScope.launch {
            val newPassDigest = messageDigest.digest(newPassword.toByteArray())
            database.userDao().updateUser(
                user.copy(password = Arrays.toString(newPassDigest))
            )
            updatePasswordState.value = UpdateProfileState.PasswordUpdateSuccess
        }
    }

    fun updateDetails(newName: String?, newHeight: Float?) {
        if (newName.isNullOrBlank() || newHeight == null || newHeight <= 0.0) {
            updatePasswordState.value = UpdateProfileState.InvalidInput
            return
        }

        val user = currentUser.value ?: return
        viewModelScope.launch {
            database.userDao().updateUser(
                user.copy(name = newName, height = newHeight)
            )
            updatePasswordState.value = UpdateProfileState.DetailsUpdateSuccess
        }
    }
}