package com.example.sudoku9x9.ui.classic.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sudoku9x9.R
import com.example.sudoku9x9.data.SudokuRepository
import com.example.sudoku9x9.data.local.ClassicCard
import com.example.sudoku9x9.ui.board.SudokuNumbersGenerator

class ClassicGameViewModel(private val repository: SudokuRepository,private val gameLevelId: Int) : ViewModel() {
    val userRecords = repository.getClassicGameUserData(gameLevelId)
    val sudokuNumbers = SudokuNumbersGenerator()

    private val _start = MutableLiveData<Boolean?>()
    val start: LiveData<Boolean?>
        get() = _start





    init {
        sudokuNumbers.getShuffleNumbersList()
    }






    fun startGame() {
        _start.value = true
    }

    fun removeStart() {
        _start.value = null
    }

    fun insertNumber(num:Int){

    }

}