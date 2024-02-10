package com.example.sudoku9x9.data

import android.content.Context
import com.example.sudoku9x9.data.local.LocalSudokuDatabase
import com.example.sudoku9x9.data.local.LocalSudokuResource
import com.example.sudoku9x9.data.remote.RemoteSudokuResource
import com.google.firebase.FirebaseApp

object ServiceLocator {

    fun createRepository(context: Context):SudokuRepository{
        FirebaseApp.initializeApp(context)
        return DefaultSudokuRepository(createLocalSudokuResource(context),RemoteSudokuResource())
    }

    private fun createLocalSudokuResource(context: Context): LocalSudokuResource {
        val database = LocalSudokuDatabase.getInstance(context)
        return LocalSudokuResource(database.sudokuDao)
    }
}