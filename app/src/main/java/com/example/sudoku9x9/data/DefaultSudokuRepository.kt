package com.example.sudoku9x9.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sudoku9x9.R
import com.example.sudoku9x9.data.local.ClassicCard
import com.example.sudoku9x9.data.local.LocalSudokuResource
import com.example.sudoku9x9.data.remote.RemoteSudokuResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DefaultSudokuRepository(
    private val localSudokuResource: LocalSudokuResource,
    private val remoteSudokuResource: RemoteSudokuResource
) : SudokuRepository {



    override fun getClassicCardsData(): LiveData<List<ClassicCard>> {
        return localSudokuResource.sudokuDao.getClassicCardsData()
    }

    override fun updateClassicCardsData(){
        CoroutineScope(Dispatchers.Main).launch {
            Log.e("qqq","0")
            withContext(Dispatchers.IO){
                Log.e("qqq","1")
                localSudokuResource.sudokuDao.insertClassicCardsData(*getCardsList().toTypedArray())
            }
        }
    }

    override fun getClassicGameUserData(gameLevelId:Int): LiveData<ClassicCard> {
        return localSudokuResource.sudokuDao.getClassicGameUserData(gameLevelId)
    }

    private fun getCardsList(): List<ClassicCard> {
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
                level = "Light",
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
            ),
            ClassicCard(
                id = 3,
                level = "Hard",
                rating = "1814",
                meanTime = "3:17:51",
                bestTime = "59:89",
                games = "87",
                record = "52:54",
                user1 = R.drawable.fot_12,
                user2 = R.drawable.fot_13,
                user3 = R.drawable.fot_15,
                user4 = R.drawable.fot_17,
                user5 = R.drawable.fot_1,
                user6 = R.drawable.fot_14,
                user7 = R.drawable.fot_5,
                user8 = R.drawable.fot_11,
                user9 = R.drawable.fot_8,
                user10 = R.drawable.fot_16,
                user11 = R.drawable.fot_18,
                user12 = R.drawable.fot_10,
            ),
            ClassicCard(
                id = 4,
                level = "Master",
                rating = "5932",
                meanTime = "5:48:23",
                bestTime = "4:49",
                games = "312",
                record = "52:54",
                user1 = R.drawable.fot_1,
                user2 = R.drawable.fot_5,
                user3 = R.drawable.fot_19,
                user4 = R.drawable.fot_11,
                user5 = R.drawable.fot_2,
                user6 = R.drawable.fot_12,
                user7 = R.drawable.fot_15,
                user8 = R.drawable.fot_13,
                user9 = R.drawable.fot_8,
                user10 = R.drawable.fot_16,
                user11 = R.drawable.fot_14,
                user12 = R.drawable.fot_18,
            )
        )
        Log.e("eeee","1 - list.size - ${list.size}")
        return list
    }


}