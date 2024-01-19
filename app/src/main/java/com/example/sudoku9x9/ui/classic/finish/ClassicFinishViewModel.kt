package com.example.sudoku9x9.ui.classic.finish

import androidx.lifecycle.ViewModel
import com.example.sudoku9x9.data.SudokuRepository

class ClassicFinishViewModel(private val repository: SudokuRepository) : ViewModel() {
    val cardsData = repository.getClassicGameUserData(1)


}