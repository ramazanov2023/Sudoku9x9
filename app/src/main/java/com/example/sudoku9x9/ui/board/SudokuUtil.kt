package com.example.sudoku9x9.ui.board

import android.util.Log
import java.lang.StringBuilder
import java.util.*

fun List<Cell>.convertToString():String{
    val sudokuNumbers = StringBuilder()
    this.map {
        val cell:String = if(it.hide){
            "${it.value}h_"
        }else{
            "${it.value}_"
        }
        sudokuNumbers.append(cell)
    }
    val result = sudokuNumbers.toString()
    Log.e("zzzz","1  -  result-$result")
    return result
}

fun String.convertToCellList():List<Cell>{
    val list = mutableListOf<Cell>()
    val scan = this.split("_")
    val clearScan = scan.dropLast(1)
    var j = 0
    Log.e("zzzz","X  -  scan.size-${clearScan.size}")
    for(i in 0 until clearScan.size){
        if(i % 9 == 0) j = 0
        val col = j++
        val row = i/9
        val cell = scan[i]
        val value = cell.first().digitToInt()
        val hide:Boolean = cell.length >= 2
        val newCell = Cell(id = i, value = value, hide = hide, hor = col, ver = row)
        list.add(newCell)
    }
    return list
}