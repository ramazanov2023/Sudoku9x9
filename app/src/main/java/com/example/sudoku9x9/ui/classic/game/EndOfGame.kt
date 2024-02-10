package com.example.sudoku9x9.ui.classic.game

import android.util.Log
import com.example.sudoku9x9.data.local.ClassicCard
import com.example.sudoku9x9.data.local.ClassicGame
import com.example.sudoku9x9.ui.classic.convertToTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EndOfGame(private val gameLevelId: Int, private val mistakes: Int, private var time: Long) {
    private var userWin = true
    private var useTime = time
    private var userLastMeanTime = 0L


    private fun checkUserGameResult() {
        if (mistakes == 3) userWin = false
        if (userWin) {
            useTime += mistakes * 2000
            userLastMeanTime = time
        } else {
            useTime = 0L
        }
    }


    fun setUserGameEndData(result: GameEndResult) {
        Log.e("suged", "1 - result-$result")
        CoroutineScope(Dispatchers.Main).launch {
            Log.e("suged", "2 - launch")
            withContext(Dispatchers.IO) {
                Log.e("suged", "3 - withContext")
                checkUserGameResult()
                Log.e("suged", "4 - checkUserGameResult")
                val lastUserGames = result.setUserLastGamesTime()
                Log.e("suged", "5 - lastUserGames-$lastUserGames")
                lastUserGames ?: return@withContext
                if (lastUserGames.isNotEmpty()) {
                    Log.e("suged", "5.1 - userLastMeanTime-$userLastMeanTime")
                    lastUserGames.forEach {
                        userLastMeanTime += it
                    }
                    userLastMeanTime /= lastUserGames.size + 1
                    Log.e("suged", "5.2 - userLastMeanTime-$userLastMeanTime")
                }
                Log.e("suged", "6 - lastUserGames.isNotEmpty()-${lastUserGames.isNotEmpty()}")

                val userRecords = result.setUserRecords() ?: return@withContext
                Log.e("suged", "7 - userRecords-$userRecords")
                getUserNewRecords(
                    userRecords,
                    userWin,
                    useTime,
                    userLastMeanTime
                ) { userNewRecords, userGame, finishMessage ->
                    Log.e("suged", "8 - userNewRecords-$userNewRecords   userGame-$userGame   finishMessage-$finishMessage")
                    result.getNewUserRecords(userNewRecords, userGame)
                    result.getGameMessage(finishMessage)
                }
            }
        }

    }

    private fun getUserNewRecords(
        records: ClassicCard,
        win: Boolean,
        userGameTime: Long,
        lastUserMeanTime: Long,
        func: (ClassicCard, ClassicGame, Pair<Boolean, String>) -> Any
    ) {
        var meanTime = lastUserMeanTime
        var bestTime = records.bestTime
        if (win) {
            bestTime = if (bestTime > 0) {
                Math.min(userGameTime, records.bestTime)
            } else {
                userGameTime
            }

        } else {
            meanTime = if (records.lastMeanTime != 0L) {
                records.lastMeanTime * 10 / 100 + records.lastMeanTime
            } else {
                0
            }
        }
        var progress = true
        var progressValue = 0L
        val finishMessage = if (meanTime < records.lastMeanTime) {
            progressValue = records.lastMeanTime - meanTime
            "Your mean time \ndecreased \nby ${progressValue.convertToTime()}s"
        } else {
            progress = false
            progressValue = meanTime - records.lastMeanTime
            "Your mean time \nincreased \nby ${progressValue.convertToTime()}s"
        }

        val card = ClassicCard(
            id = gameLevelId,
            meanTime = records.lastMeanTime,
            lastMeanTime = meanTime,
            pastBesTime = records.bestTime,
            bestTime = bestTime,
            progress = progress,
            progressValue = progressValue,
            lastTime = userGameTime,
            games = records.games + 1,
            mistakes = mistakes
        )

        val game = ClassicGame(
            gameLevelId = gameLevelId,
            time = userGameTime,
            date = System.currentTimeMillis(),
            win = win
        )

        func(card, game, Pair(win, finishMessage))
    }


    interface GameEndResult {
        fun setUserLastGamesTime(): Array<Long>
        fun setUserRecords(): ClassicCard?
        fun getNewUserRecords(records: ClassicCard, lastGame: ClassicGame)
        fun getGameMessage(message: Pair<Boolean, String>)
    }

}