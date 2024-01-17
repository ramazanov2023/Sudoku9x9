package com.example.sudoku9x9.ui.board

data class Cell(
    val ver:Int,
    val hor:Int,
    val value: Int,
    var hide: Boolean = false,
    var wrong: Boolean = false,
    var wrong_number: Int = 0,
    var id: Int = 0
)
