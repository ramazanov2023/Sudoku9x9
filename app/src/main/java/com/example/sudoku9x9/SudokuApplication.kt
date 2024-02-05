package com.example.sudoku9x9

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.example.sudoku9x9.data.ServiceLocator
import com.example.sudoku9x9.data.SudokuRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SudokuApplication : Application() {

    val repository: SudokuRepository
        get() = ServiceLocator.createRepository(applicationContext)

    override fun onCreate() {
        super.onCreate()
        Log.e("wwww","0")
        repository.updateClassicCardsData()
    }
}