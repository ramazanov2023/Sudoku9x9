package com.example.sudoku9x9

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.*
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Visibility
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var bottomNavBar: BottomNavigationView
    lateinit var windowInsetsController: WindowInsetsControllerCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavBar = findViewById<BottomNavigationView>(R.id.nav_bar)
        var toolbar = findViewById<Toolbar>(R.id.toolbar)

        val navController = findNavController(R.id.myNavHostFragment)

        setSupportActionBar(toolbar)
        var ava = toolbar.findViewById<CardView>(R.id.avatar)

        /*val appBarConfiguration = AppBarConfiguration.Builder(R.id.tasks_fragment_dest, R.id.statistics_fragment_dest)
            .setDrawerLayout(drawerLayout)
            .build()

        setupActionBarWithNavController(navController, appBarConfiguration)*/

        bottomNavBar.setupWithNavController(navController)


        ava.setOnClickListener {
            navController.navigate(R.id.profileFragment)
        }

        navController.addOnDestinationChangedListener { _, des, _ ->
            when (des.id) {
                R.id.profileFragment -> {
                    bottomNavBar.visibility = INVISIBLE
                    supportActionBar?.apply {
                        title = "Profile"
                        setDisplayHomeAsUpEnabled(true)
                    }
                }
                R.id.statisticsFragment -> {
                    bottomNavBar.visibility = VISIBLE
                    supportActionBar?.apply {
//                        title = "Statistics"
                        setDisplayHomeAsUpEnabled(false)
                    }
                }
                R.id.settingsFragment -> {
                    bottomNavBar.visibility = VISIBLE
                    supportActionBar?.apply {
                        title = "Settings"
                        setDisplayHomeAsUpEnabled(false)
                    }
                }
                R.id.playersFragment -> {
                    bottomNavBar.visibility = VISIBLE
                    supportActionBar?.apply {
                        title = "Players"
                        setDisplayHomeAsUpEnabled(false)
                    }
                }

                else -> {
                    bottomNavBar.visibility = VISIBLE
                    supportActionBar?.apply {
                        setDisplayHomeAsUpEnabled(false)
                    }
                }
            }
        }

        windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        // Configure the behavior of the hidden system bars.
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE


        /*window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            // You can hide the caption bar even when the other system bars are visible.
            // To account for this, explicitly check the visibility of navigationBars()
            // and statusBars() rather than checking the visibility of systemBars().

            if (windowInsets.hasInsets()){
                constraint.setOnClickListener {
                    // Hide both the status bar and the navigation bar.
                    windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
                }
            } else {
                constraint.setOnClickListener {
                    // Show both the status bar and the navigation bar.
                    windowInsetsController.show(WindowInsetsCompat.Type.navigationBars())
                }
            }
            view.onApplyWindowInsets(windowInsets)
        }*/
    }

    override fun onResume() {
        super.onResume()
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }


}