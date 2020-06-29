package com.hanna.balancr.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.hanna.balancr.R
import kotlinx.android.synthetic.main.fragment_login.*

//importar fragmento
class LoginFragment : Fragment(R.layout.fragment_login) {
    val loginViewModel: LoginViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        login_button.setOnClickListener {
            loginViewModel.authenticate(
                email_edit_text.toString(),
                password_edit_text.text.toString()
            )
        }

        sign_up_button.setOnClickListener {
            navController.navigate(R.id.action_login_fragment_to_register_fragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            loginViewModel.refuseAuthentication()
        }
        loginViewModel.authenticationState.observe(
            viewLifecycleOwner,
            Observer { authenticationState ->

                when (authenticationState) {
                    LoginViewModel.AuthenticationState.Authenticated -> navigateToMainView()
                    LoginViewModel.AuthenticationState.InvalidAuthentication -> notifyInvalidAuth()
                    else -> {
                        //
                    }
                }

            })
    }

    private fun notifyInvalidAuth() {
        Toast.makeText(context, "Autenticação inválida", Toast.LENGTH_LONG).show()
    }

    private fun navigateToMainView() {
        findNavController().navigate(R.id.navigation_weighting)
    }
}