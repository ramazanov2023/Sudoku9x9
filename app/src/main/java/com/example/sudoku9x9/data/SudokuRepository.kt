package com.example.sudoku9x9.data

import androidx.lifecycle.LiveData
import com.example.sudoku9x9.data.local.ClassicCard
import com.example.sudoku9x9.data.local.ClassicGame

interface SudokuRepository {

    fun getClassicCardsData(): LiveData<List<ClassicCard>>

    fun updateClassicCardsData()

    fun getClassicGameUserData(gameLevelId:Int): LiveData<ClassicCard>

    fun getLastTenGameTime():Array<Long>

    fun insertClassicCardsData(data:ClassicCard)

    fun updateClassicCardData(games: Long,
                              mistakes: Int,
                              lastMeanTime: Long,
                              meanTime: Long,
                              lastTime: Long,
                              pastBesTime: Long,
                              bestTime: Long,
                              progress:Boolean,
                              progressValue:Long,
                              gameLevelId: Int)

    fun saveClassicGame(classicGame: ClassicGame)


}