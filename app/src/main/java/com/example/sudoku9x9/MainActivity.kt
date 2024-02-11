package com.example.sudoku9x9

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Visibility
import com.bumptech.glide.Glide
import com.example.sudoku9x9.databinding.ActivityMainBinding
import com.example.sudoku9x9.ui.*
import com.example.sudoku9x9.ui.classic.finish.ClassicFinishFragmentDirections
import com.example.sudoku9x9.ui.classic.game.ClassicGameFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class TypeScreen{
    GAME,
    STATISTICS,
    SETTINGS,
    PLAYERS,
    PROFILE,
    CLASSIC_GAME,
    CLASSIC_FINISH,
}

class MainActivity : AppCompatActivity() {
//    lateinit var bottomNavBar: BottomNavigationView
    private var screen = TypeScreen.GAME
    lateinit var windowInsetsController: WindowInsetsControllerCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding:ActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)

//        setContentView(R.layout.activity_main)

//        bottomNavBar = findViewById<BottomNavigationView>(R.id.nav_bar)
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        val navController = findNavController(R.id.myNavHostFragment)

        setSupportActionBar(binding.toolbar)

        binding.navBar.setupWithNavController(navController)

        /*bottomNavBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.gameFragment -> {
                    navController.navigate(R.id.gameFragment)

                    true
                }
                R.id.statisticsFragment -> {
                    navController.navigate(R.id.statisticsFragment)
                    true
                }
                R.id.settingsFragment -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }
                R.id.playersFragment -> {
                    navController.navigate(R.id.playersFragment)
                    true
                }
                else -> false
            }
        }*/


        binding.avatar.setOnClickListener {
            when(screen){
                TypeScreen.GAME -> navController.navigate(GameFragmentDirections.actionGameFragmentToProfileFragment())
                TypeScreen.CLASSIC_GAME -> navController.navigate(ClassicGameFragmentDirections.actionClassicGameFragmentToProfileFragment())
                TypeScreen.CLASSIC_FINISH -> navController.navigate(ClassicFinishFragmentDirections.actionClassicFinishFragmentToProfileFragment())
                TypeScreen.STATISTICS -> navController.navigate(StatisticsFragmentDirections.actionStatisticsFragmentToProfileFragment())
                TypeScreen.SETTINGS -> navController.navigate(SettingsFragmentDirections.actionSettingsFragmentToProfileFragment())
                TypeScreen.PLAYERS -> navController.navigate(PlayersFragmentDirections.actionPlayersFragmentToProfileFragment())
                else -> navController.navigate(R.id.profileFragment)
            }
        }

        navController.addOnDestinationChangedListener { _, des, _ ->
            when (des.id) {
                R.id.profileFragment -> {
                    binding.navBar.visibility = INVISIBLE
                    supportActionBar?.apply {
                        title = "Profile"
                        binding.avatar.visibility = GONE
                        setDisplayHomeAsUpEnabled(true)
                    }
                }
                R.id.statisticsFragment -> {
                    binding.navBar.visibility = VISIBLE
                    supportActionBar?.apply {
//                        title = "Statistics"
                        binding.avatar.visibility = VISIBLE
                        setDisplayHomeAsUpEnabled(false)
                    }
                    screen = TypeScreen.STATISTICS
                }
                R.id.settingsFragment -> {
                    binding.navBar.visibility = VISIBLE
                    supportActionBar?.apply {
                        title = "Settings"
                        binding.avatar.visibility = VISIBLE
                        setDisplayHomeAsUpEnabled(false)
                    }
                    screen = TypeScreen.SETTINGS
                }
                R.id.playersFragment -> {
                    binding.navBar.visibility = VISIBLE
                    supportActionBar?.apply {
                        title = "Players"
                        binding.avatar.visibility = VISIBLE
                        setDisplayHomeAsUpEnabled(false)
                    }
                    screen = TypeScreen.PLAYERS
                }
                R.id.classicGameFragment -> {
                    binding.navBar.visibility = GONE
                    supportActionBar?.apply {
                        binding.avatar.visibility = VISIBLE
                        setDisplayHomeAsUpEnabled(false)
                    }
                    screen = TypeScreen.CLASSIC_GAME
                }
                R.id.classicFinishFragment -> {
                    binding.navBar.visibility = GONE
                    supportActionBar?.apply {
                        binding.avatar.visibility = VISIBLE
                        setDisplayHomeAsUpEnabled(false)
                    }
                    screen = TypeScreen.CLASSIC_FINISH
                }

                else -> {
                    binding.navBar.visibility = VISIBLE
                    supportActionBar?.apply {
                        binding.avatar.visibility = VISIBLE
                        setDisplayHomeAsUpEnabled(false)
                    }
                    screen = TypeScreen.GAME
                }
            }
        }

        windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        // Configure the behavior of the hidden system bars.
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE


        (application as SudokuApplication).repository.getUserProfile(1).observe(this, Observer {
            Glide
                .with(this)
                .load(it.userAvatar)
                .fitCenter()
                .into(binding.userAvatar)
        })

    }


    override fun onStart() {
        super.onStart()
        (application as SudokuApplication).repository.setUserSignIn()
    }


    override fun onResume() {
        super.onResume()
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }

    override fun onStop() {
        super.onStop()
        (application as SudokuApplication).repository.setUserSignOut()
    }

}