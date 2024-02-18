package com.example.sudoku9x9.data.remote

data class BattleGame(
    val firstUserId:Int? = null,
    val secondUserId:Int? = null,
    val firstUserMistakes:Int = 0,
    val secondUserMistakes:Int = 0,
    val firstUserScore:Int = 0,
    val secondUserScore:Int = 0,
    val gameStart:Boolean = false,
    val gameEnd:Boolean = false
)
