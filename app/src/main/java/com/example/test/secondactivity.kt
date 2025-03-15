package com.example.test

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.NightMode
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import java.util.Locale
import kotlin.math.log

var sharedPreferences : SharedPreferences ?= null //global
var sharedPreferences2 : SharedPreferences ?= null //global

class secondactivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var toolbar: Toolbar
    private var nightMode: Boolean=false
    private lateinit var editor : SharedPreferences.Editor
    private lateinit var pickText:TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        changeLanguage() // making to changing the language
        sharedPreferences2 = getSharedPreferences("Mode",Context.MODE_PRIVATE)

        setContentView(R.layout.activity_secondactivity)
        val navigationView: NavigationView = findViewById(R.id.navigationView)


        setUpNavigation(navigationView)
        setUpTheme(navigationView)

    }

    private fun setUpNavigation(navigationView: NavigationView){
         drawerLayout = findViewById(R.id.drawer)
         pickText=findViewById(R.id.tv_pick_in_toolbar_constraints)
         // Set up NavController and AppBarConfiguration
         val navHostFragment =
             supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
         val navController = navHostFragment.navController
         appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

         // Set up Toolbar with NavController
         toolbar = findViewById(R.id.toolbar)
         setSupportActionBar(toolbar)

         NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
         // Set up Navigation Drawer with NavController
         NavigationUI.setupWithNavController(navigationView, navController)
         // Handle Navigation Drawer item clicks
         navigationView.setNavigationItemSelectedListener { item ->
             when (item.itemId) {
                 R.id.setting -> navController.navigate(R.id.setting2)
                 R.id.category -> navController.navigate(R.id.category)
                 R.id.logout->finishAffinity()
                 //finishAffinity() finishes the current activity and all other activities

             }
             drawerLayout.closeDrawers() // Close the drawer after selecting an item
             true
         }
        // Add OnDestinationChangedListener
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.setting2) {
                pickText.text=getString(R.string.setting)
            } else {
                pickText.text=getString(R.string.pick)
            }
        }
     }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return NavigationUI.navigateUp(
            navController,
            appBarConfiguration
        ) || super.onSupportNavigateUp()
    }

    private fun changeLanguage() {
        val savedLanguage = sharedPreferences?.getString("Language", "English")
        val local: Locale = when (savedLanguage) {
            "English" -> Locale("en")
            "Arabic" -> Locale("ar")
            else -> Locale.getDefault()
        }
        val config = resources.configuration
        config.setLocale(local)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun updateToolbarTitle(title: String) {
        toolbar.title = title
    }


   private fun setUpTheme(navigationView: NavigationView){
       val menu: Menu = navigationView.menu

       // Find the menu item that contains the Switch
       val switchItem = menu.findItem(R.id.nav_dark_mode) // Replace with your Switch ID
       if(switchItem == null){
           Log.d("switchItem", "onCreate:null ")
       }
       val switchView = switchItem.actionView?.findViewById<SwitchCompat>(R.id.switchDarkMode)
       nightMode = sharedPreferences2?.getBoolean("night", false) ?: false

       if(nightMode){
           if (switchView != null) {
               AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
               switchView.isChecked= true
               switchItem?.setIcon(R.drawable.whitesun)

           }
       }

       switchView?.setOnClickListener {
           if(nightMode){
               AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
               sharedPreferences2?.edit()?.let {
                   editor = it
               } ?: Log.e("Error", "SharedPreferences2 is null")
               editor.putBoolean("night",false).commit()
               switchItem?.setIcon(R.drawable.baseline_dark_mode_24)

           }else{
               AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
               sharedPreferences2?.edit()?.let {
                   editor = it
               } ?: Log.e("Error", "SharedPreferences2 is null")
               editor.putBoolean("night",true).commit()
           }

       }
    }


}