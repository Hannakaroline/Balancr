package com.hanna.balancr.ui.sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.hanna.balancr.R
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val registrationViewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        registrationViewModel.clearSignUpState()

        confirm_password_edit_text.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                commitSignUp()
                true
            } else false
        }

        sign_up_button.setOnClickListener {
            commitSignUp()
        }

        registrationViewModel.signUpState.observe(viewLifecycleOwner, Observer { signUpState ->
            when (signUpState) {
                SignUpViewModel.SignUpState.InvalidInput -> notify(R.string.invalid_input)
                SignUpViewModel.SignUpState.NewPasswordsDontMatch -> notify(R.string.passwords_dont_match)
                SignUpViewModel.SignUpState.InvalidEmail -> notify(R.string.invalid_email)
                SignUpViewModel.SignUpState.EmailAlreadyTaken -> notify(R.string.email_already_taken)
                SignUpViewModel.SignUpState.Completed -> navController.popBackStack(
                    R.id.login_fragment,
                    false
                )
                else -> {
                }
            }
        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navController.popBackStack(R.id.login_fragment, false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().popBackStack(R.id.login_fragment, false)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun commitSignUp() {
        registrationViewModel.createAccount(
            name_edit_text.text?.toString(),
            height_edit_text.text?.toString()?.toFloatOrNull(),
            email_edit_text.text?.toString(),
            password_edit_text.text?.toString(),
            confirm_password_edit_text.text?.toString()
        )
    }

    private fun notify(errorStringId: Int) {
        Toast.makeText(context, errorStringId, Toast.LENGTH_SHORT).show()
        registrationViewModel.clearSignUpState()
    }
}