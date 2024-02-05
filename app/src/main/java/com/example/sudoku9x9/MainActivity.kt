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
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Visibility
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
    lateinit var bottomNavBar: BottomNavigationView
    private var screen = TypeScreen.GAME
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

        bottomNavBar

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




        ava.setOnClickListener {
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
                    screen = TypeScreen.STATISTICS
                }
                R.id.settingsFragment -> {
                    bottomNavBar.visibility = VISIBLE
                    supportActionBar?.apply {
                        title = "Settings"
                        setDisplayHomeAsUpEnabled(false)
                    }
                    screen = TypeScreen.SETTINGS
                }
                R.id.playersFragment -> {
                    bottomNavBar.visibility = VISIBLE
                    supportActionBar?.apply {
                        title = "Players"
                        setDisplayHomeAsUpEnabled(false)
                    }
                    screen = TypeScreen.PLAYERS
                }
                R.id.classicGameFragment -> {
                    bottomNavBar.visibility = GONE
                    supportActionBar?.apply {
                        setDisplayHomeAsUpEnabled(false)
                    }
                    screen = TypeScreen.CLASSIC_GAME
                }
                R.id.classicFinishFragment -> {
                    bottomNavBar.visibility = GONE
                    supportActionBar?.apply {
                        setDisplayHomeAsUpEnabled(false)
                    }
                    screen = TypeScreen.CLASSIC_FINISH
                }

                else -> {
                    bottomNavBar.visibility = VISIBLE
                    supportActionBar?.apply {
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

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO){
                val auth = FirebaseAuth.getInstance()
                val userProfile = (application as SudokuApplication).repository.checkRegistration()
                if(userProfile.signUp){
                    auth.signInWithEmailAndPassword(userProfile.userEmail, userProfile.userPassword)
                        .addOnCompleteListener(this@MainActivity) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("sasasa", "signInWithEmail:success")
                                val user = auth.currentUser
//                                updateUI(user)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("sasasa", "signInWithEmail:failure", task.exception)
                                Toast.makeText(
                                    baseContext,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT,
                                ).show()
//                                updateUI(null)
                            }
                        }
                }
            }
        }
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