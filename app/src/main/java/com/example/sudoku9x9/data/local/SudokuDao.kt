package com.example.sudoku9x9.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface SudokuDao {

    // for Classic

    @Query("SELECT * FROM classic_card_table")
    fun getClassicCardsData():LiveData<List<ClassicCard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertClassicCardsData(vararg card:ClassicCard)






}