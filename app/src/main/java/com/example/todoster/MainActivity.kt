package com.example.todoster

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var isAuthenticated = false
    private var isInitialized = false
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition { !isInitialized }

        setContentView(R.layout.main_activity)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.nav_host_fragment)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupNavigation()

        if (savedInstanceState == null) {
            performInitialization()
        } else {
            isInitialized = true
        }
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Navigation Controller handles back navigation automatically
        // No custom OnBackPressedCallback needed!
    }

    private fun performInitialization() {
        lifecycleScope.launch {
            checkAuthentication()

            delay(1000)

            isInitialized = true

            navigateToAppropriateScreen()
        }
    }

    private suspend fun checkAuthentication() {
        delay(500)
        isAuthenticated = false
    }

    private fun navigateToAppropriateScreen() {
        if (isAuthenticated) {
            navController.navigate(R.id.action_onboarding_to_main)
        }
    }
}