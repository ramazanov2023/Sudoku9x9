package com.example.sudoku9x9.ui.classic.finish

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sudoku9x9.data.SudokuRepository

class ClassicFinishViewModelFactory(private val repository: SudokuRepository, private val gameLevelId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ClassicFinishViewModel::class.java)){
            return ClassicFinishViewModel(repository,gameLevelId) as T
        }
        throw IllegalArgumentException("Factory Error")
    }
}