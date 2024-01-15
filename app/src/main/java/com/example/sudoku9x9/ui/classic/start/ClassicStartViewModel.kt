package com.example.sudoku9x9.ui.classic.start

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sudoku9x9.R
import com.example.sudoku9x9.data.SudokuRepository
import com.example.sudoku9x9.data.local.ClassicCard

class ClassicStartViewModel(private val repository: SudokuRepository) : ViewModel() {
    val cardsData = repository.getClassicCardsData()

    private val _start = MutableLiveData<Boolean?>()
    val start: LiveData<Boolean?>
        get() = _start

    fun startGame() {
        _start.value = true
    }

    fun removeStart() {
        _start.value = null
    }

    /*private val _cards = MutableLiveData<List<ClassicCard>>()
    val cards:LiveData<List<ClassicCard>>
        get() = getCardsList()

    private fun getCardsList(): LiveData<List<ClassicCard>> {
        val list = listOf(
            ClassicCard(
                id = 1,
                level = "Fast",
                rating = "3289",
                meanTime = "58:64",
                bestTime = "54:32",
                games = "123",
                record = "48:57",
                user1 = R.drawable.fot_11,
                user2 = R.drawable.fot_1,
                user3 = R.drawable.fot_10,
                user4 = R.drawable.fot_14,
                user5 = R.drawable.fot_15,
                user6 = R.drawable.fot_8,
                user7 = R.drawable.fot_2,
                user8 = R.drawable.fot_5,
                user9 = R.drawable.fot_12,
                user10 = R.drawable.fot_16,
                user11 = R.drawable.fot_13,
                user12 = R.drawable.fot_17,
            ),
            ClassicCard(
                id = 2,
                level = "Hard",
                rating = "2476",
                meanTime = "1:12:23",
                bestTime = "59:89",
                games = "92",
                record = "52:54",
                user1 = R.drawable.fot_5,
                user2 = R.drawable.fot_15,
                user3 = R.drawable.fot_10,
                user4 = R.drawable.fot_13,
                user5 = R.drawable.fot_1,
                user6 = R.drawable.fot_12,
                user7 = R.drawable.fot_2,
                user8 = R.drawable.fot_11,
                user9 = R.drawable.fot_17,
                user10 = R.drawable.fot_16,
                user11 = R.drawable.fot_14,
                user12 = R.drawable.fot_8,
            )
        )
        _cards.value = list

        Log.e("eeee","1 - list.size - ${list.size}")
        return _cards
    }*/
}