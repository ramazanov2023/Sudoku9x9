package com.example.sudoku9x9.ui.classic.start

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sudoku9x9.R
import com.example.sudoku9x9.data.SudokuRepository
import com.example.sudoku9x9.data.local.ClassicCard

class ClassicStartViewModel(private val repository: SudokuRepository) : ViewModel() {
    val cardsData = repository.getClassicCardsData()

    private val _start = MutableLiveData<Boolean?>()
    val start: LiveData<Boolean?>
        get() = _start

    fun startGame() {
        _start.value = true
    }

    fun removeStart() {
        _start.value = null
    }
}