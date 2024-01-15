package com.example.sudoku9x9.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "classic_card_table")
data class ClassicCard(

    @PrimaryKey
    val id:Int,
    val level:String,
    val rating:String,
    val meanTime:String,
    val bestTime:String,
    val games:String,
    val record:String,
    val user1:Int,
    val user2:Int,
    val user3:Int,
    val user4:Int,
    val user5:Int,
    val user6:Int,
    val user7:Int,
    val user8:Int,
    val user9:Int,
    val user10:Int,
    val user11:Int,
    val user12:Int,
)