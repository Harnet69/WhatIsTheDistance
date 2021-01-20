package com.harnet.whatisthedistance.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.harnet.whatisthedistance.R
import com.harnet.whatisthedistance.util.SharedPreferencesHelper

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpNavigation()
    }

    private fun setUpNavigation() {
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_bar)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment?
        navHostFragment?.let {
            NavigationUI.setupWithNavController(
                bottomNavigationView,
                it.navController
            )
        }
    }
}