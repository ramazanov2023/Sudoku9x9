package com.example.sudoku9x9

import android.app.Application
import android.content.ComponentCallbacks
import android.util.Log
import com.example.sudoku9x9.data.ServiceLocator
import com.example.sudoku9x9.data.SudokuRepository

class SudokuApplication : Application() {

    val repository: SudokuRepository
        get() = ServiceLocator.createRepository(applicationContext)

    override fun onCreate() {
        super.onCreate()
        Log.e("jjjj","0")
        repository.updateUserData()
    }
}