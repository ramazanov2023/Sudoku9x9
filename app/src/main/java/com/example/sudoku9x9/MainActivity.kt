package com.example.sudoku9x9

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.*
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Visibility
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var bottomNavBar: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavBar = findViewById<BottomNavigationView>(R.id.nav_bar)
        var toolbar = findViewById<Toolbar>(R.id.toolbar)

        val navController = findNavController(R.id.myNavHostFragment)

        setSupportActionBar(toolbar)
//        var ava = toolbar.findViewById<ImageView>(R.id.user_avatar)
        var ava = toolbar.findViewById<CardView>(R.id.avatar)

        /*val appBarConfiguration = AppBarConfiguration.Builder(R.id.tasks_fragment_dest, R.id.statistics_fragment_dest)
            .setDrawerLayout(drawerLayout)
            .build()

        setupActionBarWithNavController(navController, appBarConfiguration)*/

        bottomNavBar.setupWithNavController(navController)

//        bottomNavBar.visibility = INVISIBLE

        ava.setOnClickListener {
//            Navigation.findNavController(it).navigate(R.id.profileFragment)
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

                else -> {
                    bottomNavBar.visibility = VISIBLE
                    supportActionBar?.apply {
                        title = "Profile"
                        setDisplayHomeAsUpEnabled(false)
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }


}