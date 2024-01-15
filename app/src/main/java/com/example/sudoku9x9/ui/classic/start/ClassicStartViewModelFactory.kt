package com.example.sudoku9x9.ui.classic.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sudoku9x9.data.SudokuRepository

class ClassicStartViewModelFactory(val repository: SudokuRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ClassicStartViewModel::class.java)){
            return ClassicStartViewModel(repository) as T
        }
        throw IllegalArgumentException("Factory Error")
    }
}