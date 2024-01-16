package com.example.sudoku9x9.data

import androidx.lifecycle.LiveData
import com.example.sudoku9x9.data.local.ClassicCard

interface SudokuRepository {

    fun getClassicCardsData(): LiveData<List<ClassicCard>>

    fun updateClassicCardsData()

    fun getClassicGameUserData(gameLevelId:Int): LiveData<ClassicCard>

}