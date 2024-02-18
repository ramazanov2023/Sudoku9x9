package com.example.sudoku9x9.data.remote

data class BattleRequest(
    val gameId: String? = null,
    val sudokuNumbers: String? = null,
    val accept: Boolean = false
)
