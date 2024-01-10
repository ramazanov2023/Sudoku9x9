package com.example.sudoku9x9

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var bottomNavBar = findViewById<BottomNavigationView>(R.id.nav_bar)

        bottomNavBar.setupWithNavController(findNavController(R.id.myNavHostFragment))

    }
}