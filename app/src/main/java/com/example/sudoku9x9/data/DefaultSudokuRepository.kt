package com.example.sudoku9x9.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.sudoku9x9.R
import com.example.sudoku9x9.data.local.ClassicCard
import com.example.sudoku9x9.data.local.LocalSudokuResource
import com.example.sudoku9x9.data.local.Profile
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

    override fun updateClassicCardsData() {
        CoroutineScope(Dispatchers.Main).launch {
            Log.e("qqq", "0")
            withContext(Dispatchers.IO) {
                val check = localSudokuResource.sudokuDao.checkFirstLaunch(1)
                if(!check) {
                    Log.e("qqq", "1")
                    localSudokuResource.sudokuDao.insertClassicCardsData(*getCardsList().toTypedArray())
                    localSudokuResource.sudokuDao.insertUserProfile(Profile(id = 1))
                    localSudokuResource.sudokuDao.setFirstLaunch(true,1)
                }
            }
        }
    }




    override fun updateClassicCardData(
        games: Long,
        meanTime: Long,
        bestTime: Long,
        gameLevelId: Int
    ) {
        localSudokuResource.sudokuDao.updateClassicCardData(games = games,meanTime = meanTime,bestTime = bestTime,gameLevelId = gameLevelId)
    }

    override fun insertClassicCardsData(data: ClassicCard) {
        localSudokuResource.sudokuDao.insertClassicCardsData(data)
    }

    override fun getClassicGameUserData(gameLevelId: Int): LiveData<ClassicCard> {
        return localSudokuResource.sudokuDao.getClassicGameUserData(gameLevelId)
    }

    override fun getLastTenGameTime(): Array<Long> {
        return arrayOf(36700,40510,32590,47120,31568,36700,40510,32590,47120)
    }

    private fun getCardsList(): List<ClassicCard> {
        val list = listOf(
            ClassicCard(
                id = 1,
                level = "Fast",
                rating = "3289",
                meanTime = 58360,
                bestTime = 56470,
                games = 0,
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
                meanTime = 163360,
                bestTime = 158470,
                games = 0,
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
                meanTime = 317360,
                bestTime = 304470,
                games = 0,
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
                meanTime = 58360,
                bestTime = 56470,
                games = 0,
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
        Log.e("eeee", "1 - list.size - ${list.size}")
        return list
    }


}