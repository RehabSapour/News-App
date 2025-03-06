package com.example.test

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.test.api.NewsViewModel
import com.example.test.ui.ListFragment
import com.google.android.material.navigation.NavigationView
import java.util.Locale
import kotlin.math.log

var sharedPreferences : SharedPreferences ?= null //global
class secondactivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var toolbar: Toolbar
    lateinit var searchView: SearchView
    private val viewModel: NewsViewModel by viewModels()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Apply saved language setting
        Log.d("secondActivity", "onCreate: ")
        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        changeLanguage() // making to changing the language
        setContentView(R.layout.activity_secondactivity)

        /////////////////////////////////////////////////////////////////////////





         searchView = findViewById(R.id.searchView)
        val q = searchView.query.toString()

//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
////                if (!query.isNullOrEmpty()) {
////                    viewModel.setsearchQuery(query)
////                }
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                newText?.let {
//                    viewModel.setsearchQuery(it)
//                }
//                return true
//            }
//        })
        ////////////////////////////////////////////////////////////////////////////////////

        drawerLayout = findViewById(R.id.drawer)
        val navigationView: NavigationView = findViewById(R.id.navigationView)

        // Set up NavController and AppBarConfiguration
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)


        // Set up Toolbar with NavController
         toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        /////////////// search menu //////////////////////////////


        //////////////////////////////////
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        // Set up Navigation Drawer with NavController
        NavigationUI.setupWithNavController(navigationView, navController)
        // Handle Navigation Drawer item clicks
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.setting -> navController.navigate(R.id.setting2)
                R.id.category -> navController.navigate(R.id.category)
                R.id.favorite -> navController.navigate(R.id.favorite)
            }
            drawerLayout.closeDrawers() // Close the drawer after selecting an item
            true
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

}