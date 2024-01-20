package com.example.sudoku9x9.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ClassicCard::class,Profile::class,ClassicGame::class], version = 1, exportSchema = false)
abstract class LocalSudokuDatabase:RoomDatabase() {
    abstract val sudokuDao:SudokuDao

    companion object{
        private var INSTANCE:LocalSudokuDatabase? = null
        fun getInstance(context: Context):LocalSudokuDatabase{
            return INSTANCE ?: synchronized(this){
                INSTANCE = Room.databaseBuilder(
                    context,
                    LocalSudokuDatabase::class.java,
                    "sudoku_9x9_database"
                ).build()

                return INSTANCE as LocalSudokuDatabase
            }
        }
    }
}