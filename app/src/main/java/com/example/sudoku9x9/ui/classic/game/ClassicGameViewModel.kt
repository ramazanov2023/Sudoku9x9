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

    private val _number = MutableLiveData<Int>()
    val number: LiveData<Int>
        get() = _number





    init {
        sudokuNumbers.getShuffleNumbersList()
    }






    fun insertNumber(num:Int){
        _number.value = num
    }

}