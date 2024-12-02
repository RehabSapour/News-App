package com.example.test

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import java.util.Locale
var sharedPreferences : SharedPreferences?=null //global
class secondactivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Apply saved language setting
         sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
         changeLanguage() // making to changing the language

        setContentView(R.layout.activity_secondactivity)

        drawerLayout = findViewById(R.id.drawer)
        val navigationView: NavigationView = findViewById(R.id.navigationView)

        // Set up NavController and AppBarConfiguration
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        // Set up Toolbar with NavController
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        //////////////////////////////////

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        // Set up Navigation Drawer with NavController
        NavigationUI.setupWithNavController(navigationView, navController)
        // Handle Navigation Drawer item clicks
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.setting -> navController.navigate(R.id.setting2)
                R.id.category -> navController.navigate(R.id.category)
                R.id.favorite ->navController.navigate(R.id.favorite)
            }
            drawerLayout.closeDrawers() // Close the drawer after selecting an item
            true
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun changeLanguage() {
        val savedLanguage= sharedPreferences?.getString("Language","English")
        val local:Locale = when(savedLanguage){
            "English" -> Locale("en")
            "Arabic" -> Locale("ar")
            else -> Locale.getDefault()
        }
        val config = resources.configuration
        config.setLocale(local)
        resources.updateConfiguration(config,resources.displayMetrics)
    }
}