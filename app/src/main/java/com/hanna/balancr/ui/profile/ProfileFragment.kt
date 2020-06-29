package com.hanna.balancr.ui.profile

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.hanna.balancr.R
import com.hanna.balancr.model.entities.User
import com.hanna.balancr.ui.login.LoginViewModel
import com.hanna.balancr.ui.profile.ProfileViewModel.UpdateProfileState
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    val profileViewModel: ProfileViewModel by activityViewModels()
    val loginViewModel: LoginViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logout_button.setOnClickListener {
            loginViewModel.logout()
        }

        profileViewModel.currentUser.observe(viewLifecycleOwner, Observer {
            updateUserProfileUi(it)
        })

        profileViewModel.updatePasswordState.observe(
            viewLifecycleOwner,
            Observer { updatePasswordState ->
                when (updatePasswordState) {
                    UpdateProfileState.InvalidInput -> notify(R.string.invalid_input)
                    UpdateProfileState.IncorrectPassword -> notify(R.string.incorrect_password)
                    UpdateProfileState.NewPasswordsDontMatch -> notify(R.string.passwords_dont_match)
                    UpdateProfileState.PasswordUpdateSuccess -> {
                        hideKeyboard()
                        notify(R.string.password_updated)
                    }
                    UpdateProfileState.DetailsUpdateSuccess -> {
                        hideKeyboard()
                        notify(R.string.details_updated)
                    }
                    else -> {
                    }
                }
            })

        confirm_password_edit_text.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                commitPasswordChange()
                true
            } else false
        }

        confirm_password_button.setOnClickListener {
            commitPasswordChange()
        }

        height_edit_text.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                commitDetailsChange()
                true
            } else false
        }

        save_details_button.setOnClickListener {
            commitDetailsChange()
        }
    }

    private fun commitDetailsChange() {
        profileViewModel.updateDetails(
            name_edit_text.text?.toString(),
            height_edit_text.text?.toString()?.toFloatOrNull()
        )
    }

    private fun commitPasswordChange() {
        profileViewModel.updatePasswordWith(
            current_password_edit_text.text?.toString(),
            new_password_edit_text.text?.toString(),
            confirm_password_edit_text.text?.toString()
        )
    }

    private fun notify(errorStringId: Int) {
        Toast.makeText(context, errorStringId, Toast.LENGTH_SHORT).show()
        profileViewModel.clearUpdatePasswordState()
    }

    private fun updateUserProfileUi(user: User?) {
        if (user == null) {
            email_edit_text.setText("")
            name_edit_text.setText("")
            height_edit_text.setText("")
        } else {
            email_edit_text.setText(user.email)
            name_edit_text.setText(user.name)
            height_edit_text.setText(String.format("%.2f", user.height))
        }
    }

    private fun hideKeyboard() {
        val inputManager = activity?.getSystemService<InputMethodManager>()
        view?.run {
            inputManager?.hideSoftInputFromWindow(windowToken, 0)
        }
    }
}