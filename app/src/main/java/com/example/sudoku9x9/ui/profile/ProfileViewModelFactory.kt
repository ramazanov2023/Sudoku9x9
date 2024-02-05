package com.example.sudoku9x9.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sudoku9x9.data.SudokuRepository

class ProfileViewModelFactory(private val repository:SudokuRepository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ProfileViewModel::class.java)){
            return ProfileViewModel(repository) as T
        }else{
            throw IllegalArgumentException("Error")
        }
    }
}