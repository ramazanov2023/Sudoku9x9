package com.example.sudoku9x9.data

import android.content.Context
import com.example.sudoku9x9.data.local.LocalSudokuDatabase
import com.example.sudoku9x9.data.local.LocalSudokuResource
import com.example.sudoku9x9.data.remote.RemoteSudokuResource
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

object ServiceLocator {

    fun createRepository(context: Context):SudokuRepository{
        return DefaultSudokuRepository(createLocalSudokuResource(context),createRemoteSudokuResource(context))
    }

    private fun createLocalSudokuResource(context: Context): LocalSudokuResource {
        val roomDatabase = LocalSudokuDatabase.getInstance(context)
        return LocalSudokuResource(roomDatabase.sudokuDao)
    }

    private fun createRemoteSudokuResource(context: Context): RemoteSudokuResource {
        FirebaseApp.initializeApp(context)
        val firebaseDatabase = FirebaseDatabase.getInstance("https://sudoku9x9-276cf-default-rtdb.europe-west1.firebasedatabase.app/")
        val firebaseStorage = FirebaseStorage.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()
        return RemoteSudokuResource(firebaseDatabase,firebaseStorage,firebaseAuth)
    }
}