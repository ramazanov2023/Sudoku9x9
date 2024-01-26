package com.example.sudoku9x9.ui.board

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

const val FAST_LEVEL = 20
const val LIGHT_LEVEL = 30
const val HARD_LEVEL = 40
const val MASTER_LEVEL = 46

class SudokuNumbersGenerator(private val gameLevelId: Int) {

    private val _remainNumbersLiveData = MutableLiveData<List<Int>?>()
    val remainNumbersLiveData: LiveData<List<Int>?>
        get() = _remainNumbersLiveData

    private val _numbersLiveData = MutableLiveData<List<Cell>>()
    val numbersLiveData: LiveData<List<Cell>>
        get() = _numbersLiveData

    private var firstArray: Array<Array<Array<Int>>>? = null

    private fun createStartNumbersList(): Array<Array<Array<Int>>> {
        var thirdArray1 = arrayOf(9, 6, 2, 3, 7, 8, 4, 1, 5)
        var thirdArray2 = arrayOf(1, 8, 5, 4, 2, 9, 7, 6, 3)
        var thirdArray3 = arrayOf(3, 7, 4, 5, 6, 1, 9, 2, 8)

        var thirdArray4 = arrayOf(2, 1, 8, 7, 4, 5, 3, 9, 6)
        var thirdArray5 = arrayOf(4, 9, 6, 8, 3, 2, 1, 5, 7)
        var thirdArray6 = arrayOf(7, 5, 3, 1, 9, 6, 2, 8, 4)

        var thirdArray7 = arrayOf(5, 3, 1, 9, 8, 4, 6, 7, 2)
        var thirdArray8 = arrayOf(6, 4, 9, 2, 5, 7, 8, 3, 1)
        var thirdArray9 = arrayOf(8, 2, 7, 6, 1, 3, 5, 4, 9)


        var secondArray1 = arrayOf(thirdArray1, thirdArray2, thirdArray3)
        var secondArray2 = arrayOf(thirdArray4, thirdArray5, thirdArray6)
        var secondArray3 = arrayOf(thirdArray7, thirdArray8, thirdArray9)


        var firstArray = arrayOf(secondArray1, secondArray2, secondArray3)
        return firstArray
    }

    private fun changeOrientation(array: Array<Array<Array<Int>>>): Array<Array<Array<Int>>> {
        val changedArray = getFalseArray()

        var d = 0
        var e = 0
        var g = 0

        for (a in array) {
            for (b in a) {
                for (c in b) {
                    changedArray[d][e][g] = c
                    e++
                    if (e > 2) {
                        e = 0
                        d++
                    }
                }
                d = 0
                e = 0
//            println(g)
                g++
            }
        }

        return changedArray

    }

    fun getShuffleNumbersList() {
        firstArray ?: synchronized(this) {
            firstArray = createStartNumbersList()
        }

        var phase1 = mixMainArray(firstArray!!)
        var phase2 = changeOrientation(phase1)
        var phase3 = mixMainArray(phase2)

        var phase4 = getNumbers(phase3)
        hideRandomNumbers(phase4)
        Log.e("asas", "2  getShuffleNumbersList")
    }

    private fun getNumbers(array: Array<Array<Array<Int>>>): List<Cell> {
        val list = arrayListOf<Cell>()

        var cellId = 0
        var ver = 0
        for (i in array) {
            for (a in i) {
                for (hor in 0..8) {
                    list.add(Cell(hor = hor, ver = ver, value = a[hor], id = cellId))
                    cellId++
                }
                ver++
            }
        }

        return list
    }

    private fun hideRandomNumbers(list: List<Cell>) {
        val numbers: List<Cell> = list
        var remainNumbers = mutableListOf<Int>(0, 0, 0, 0, 0, 0, 0, 0, 0)
        var r = Random()
        var repeat = 0
        var counter = 0
        val level = when (gameLevelId) {
            1 -> FAST_LEVEL
            2 -> LIGHT_LEVEL
            3 -> HARD_LEVEL
            4 -> MASTER_LEVEL
            else -> FAST_LEVEL
        }

        while (repeat < level) {
            val cell = r.nextInt(80)
            if (!numbers[cell].hide) {

                numbers[cell].hide = true
                repeat++
            } else {
            }
            Log.e("asas", "$counter  -  $repeat")
            counter++

        }

        _numbersLiveData.value = numbers

        numbers.forEach {
            if (it.hide) {
                val numValue = it.value - 1
                Log.e("cccc", "numValue - $numValue")
                var num = remainNumbers[numValue]
                Log.e("cccc", "num - $num")
//            remainNumbers.add(numValue,++num)
                remainNumbers[numValue] = ++num
                Log.e("cccc", "remainNumbers[numValue] - ${remainNumbers[numValue]}")
            }
        }

        _remainNumbersLiveData.value = remainNumbers

    }

    fun decreasedRemainNumbers(action: Int, number: Int) {
        _remainNumbersLiveData.value?.let {
            val list = it.toMutableList()
            val newValue = when (action) {
                REMAIN_NUMBERS_DECREASE -> list[number] - 1
                REMAIN_NUMBERS_INCREASE -> list[number] + 1
                else -> list[number]
            }
            list[number] = newValue
            _remainNumbersLiveData.value = list

        }
    }


    private fun printMainArray(array: Array<Array<Array<Int>>>) {
        for (i in array) {
            for (a in i) {
                for (b in a) {
                    print("$b ")
                }
                println()
            }
        }
        println()
    }

    private fun mixMainArray(array: Array<Array<Array<Int>>>): Array<Array<Array<Int>>> {
        array.shuffle()
        for (i in array) {
            i.shuffle()
        }
        return array
    }

    private fun getFalseArray(): Array<Array<Array<Int>>> {
        var thirdArray1 = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
        var thirdArray2 = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
        var thirdArray3 = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)

        var thirdArray4 = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
        var thirdArray5 = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
        var thirdArray6 = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)

        var thirdArray7 = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
        var thirdArray8 = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
        var thirdArray9 = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)


        var secondArray1 = arrayOf(thirdArray1, thirdArray2, thirdArray3)
        var secondArray2 = arrayOf(thirdArray4, thirdArray5, thirdArray6)
        var secondArray3 = arrayOf(thirdArray7, thirdArray8, thirdArray9)


        var firstArray = arrayOf(secondArray1, secondArray2, secondArray3)

        return firstArray
    }
}

