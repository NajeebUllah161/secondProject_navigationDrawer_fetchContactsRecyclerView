package com.example.navigationdrawerexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        val navigationView = findViewById<NavigationView>(R.id.navigation_side_drawer)
        NavigationUI.setupWithNavController(navigationView, navController)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            navController,
            this.findViewById<DrawerLayout>(R.id.drawer_layout),

        )
    }
}