package com.example.sudoku9x9.ui.battle.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sudoku9x9.data.SudokuRepository

class BattleGameViewModelFactory(private val repository: SudokuRepository, private val gameLevelId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BattleGameViewModel::class.java)){
            return BattleGameViewModel(repository,gameLevelId) as T
        }
        throw IllegalArgumentException("Factory Error")
    }
}