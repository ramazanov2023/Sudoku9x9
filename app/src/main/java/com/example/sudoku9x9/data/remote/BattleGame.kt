package com.example.sudoku9x9.data.remote

data class BattleGame(
    val sudokuNumbers:String? = null,
    var firstUserId:String? = null,
    var secondUserId:String? = null,
    var firstUserMistakes:Int = 0,
    var secondUserMistakes:Int = 0,
    var firstUserScore:Int = 0,
    var secondUserScore:Int = 0,
    var gameStart:Boolean = false,
    var gameEnd:Boolean = false
)
