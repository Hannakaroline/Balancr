package com.hanna.balancr

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.hanna.balancr.ui.login.LoginViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.nav_host_fragment)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_weighting,
                R.id.navigation_body_pictures,
                R.id.navigation_profile,
                R.id.login_fragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        viewModel.authenticationState.observe(this, Observer {
            if (it == LoginViewModel.AuthenticationState.UnAuthenticated || it == LoginViewModel.AuthenticationState.InvalidAuthentication) {
                navController.navigate(R.id.login_fragment)
            } else {
                navController.popBackStack()
                hideKeyboard()
                navController.navigate(R.id.navigation_weighting)
            }
        })

        navController.addOnDestinationChangedListener { _, destination, _ ->
            navView.visibility = when (destination.id) {
                R.id.navigation_weighting, R.id.navigation_body_pictures, R.id.navigation_profile -> View.VISIBLE
                else -> View.GONE
            }
        }
    }

    private fun hideKeyboard() {
        val inputManager = getSystemService<InputMethodManager>()
        inputManager?.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }
}
