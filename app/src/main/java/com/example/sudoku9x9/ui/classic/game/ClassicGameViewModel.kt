package com.example.sudoku9x9.ui.classic.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sudoku9x9.data.SudokuRepository
import com.example.sudoku9x9.ui.board.SudokuNumbersGenerator

class ClassicGameViewModel(private val repository: SudokuRepository,private val gameLevelId: Int) : ViewModel() {
    val userRecords = repository.getClassicGameUserData(gameLevelId)
    val sudokuNumbers = SudokuNumbersGenerator()

    private val _number = MutableLiveData<Int>()
    val number: LiveData<Int>
        get() = _number

    private val _inactiveNumber = MutableLiveData<Int?>()
    val inactiveNumber: LiveData<Int?>
        get() = _inactiveNumber




    init {
        sudokuNumbers.getShuffleNumbersList()
    }






    fun insertNumber(num:Int){
        _number.value = num
    }

    fun addInactiveNumber(num:Int){
        _inactiveNumber.value = num
    }

}