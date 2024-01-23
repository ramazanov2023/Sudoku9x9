package com.example.sudoku9x9.ui.classic.game

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sudoku9x9.R
import com.example.sudoku9x9.data.SudokuRepository
import com.example.sudoku9x9.data.local.ClassicCard
import com.example.sudoku9x9.data.local.ClassicGame
import com.example.sudoku9x9.data.local.FAST_LEVEL
import com.example.sudoku9x9.ui.board.SudokuNumbersGenerator
import com.example.sudoku9x9.ui.classic.convertToTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ClassicGameViewModel(private val repository: SudokuRepository, private val gameLevelId: Int) :
    ViewModel() {
    private lateinit var downTimer: CountDownTimer
    var mistakes = 0

    val userRecords = repository.getClassicGameUserData(gameLevelId)
    val sudokuNumbers = SudokuNumbersGenerator()

    private val _number = MutableLiveData<Int>()
    val number: LiveData<Int>
        get() = _number

    private val _timer = MutableLiveData<String?>()
    val timer: LiveData<String?>
        get() = _timer

    private val _finishGame = MutableLiveData<Pair<Boolean,String>?>()
    val finishGame: LiveData<Pair<Boolean,String>?>
        get() = _finishGame


    init {
        sudokuNumbers.getShuffleNumbersList()
        turnOnTimer()
    }

    private fun turnOnTimer() {

        downTimer = object : CountDownTimer(600000, 10) {
            var min = 0
            var sec = 0
            var mls = 0
            var mlsLong: Long = 0
            override fun onTick(millisUntilFinished: Long) {
                mlsLong += 10L
                mls += 10
                if (mls == 1000) {
                    sec++
                    mls = 0
                }
                if (sec == 60) {
                    min++
                    sec = 0
                }
                if (min > 0) {
                    _timer.postValue("$min:$sec")
                } else {
                    _timer.postValue("$sec")
                }

            }

            override fun onFinish() {
                Log.e("nnnn","3  onFinish-$mistakes")
                viewModelScope.launch {
                    Log.e("nnnn","4  viewModelScope")
                    var win = true
                    if (mistakes == 3) win = false
                    var lastMeanTime = 0L
                    if (win) {
                        mlsLong+= mistakes*2000
                        lastMeanTime = mlsLong
                    } else {
                        mlsLong = 0L
                    }
                    Log.e("nnnn","5  win-$win   lastMeanTime-$lastMeanTime   mlsLong-$mlsLong")
                    withContext(Dispatchers.IO) {
                        var finishMessage = ""
                        val lastTenGames = repository.getLastTenGameTime()
                        if (lastTenGames.isNotEmpty()) {
                            repository.getLastTenGameTime().forEach {
                                lastMeanTime += it
                            }
                            lastMeanTime /= lastTenGames.size + 1
                        }

                        val lastGameData = userRecords.value

                        lastGameData?.let {
                            Log.e("nnnn","6  lastMeanTime-$lastMeanTime   mlsLong-$mlsLong")
                            var bestTime = it.bestTime
                            if (win) {
                                bestTime = Math.min(mlsLong, it.bestTime)
                            } else {
                                lastMeanTime = if(it.lastMeanTime!=0L) {
                                    it.lastMeanTime * 10 / 100 + it.lastMeanTime
                                }else{
                                    0
                                }
                            }
                            var progress = true
                            var progressValue = 0L
                            if(lastMeanTime<it.lastMeanTime){
                                progressValue = it.lastMeanTime - lastMeanTime
                                finishMessage = "Your mean time \ndecreased \nby ${progressValue.convertToTime()}s"
                            }else{
                                progress = false
                                progressValue = lastMeanTime - it.lastMeanTime
                                finishMessage = "Your mean time \nincreased \nby ${progressValue.convertToTime()}s"
                            }

                            Log.e("nnnn","7  progress-$progress   finishMessage-$finishMessage")
                            repository.updateClassicCardData(
                                games = it.games + 1,
                                mistakes = mistakes,
                                lastMeanTime = lastMeanTime,
                                meanTime = it.lastMeanTime,
                                lastTime = mlsLong,
                                pastBesTime = it.bestTime,
                                bestTime = bestTime,
                                progress = progress,
                                progressValue = progressValue,
                                gameLevelId = FAST_LEVEL
                            )
                            repository.saveClassicGame(
                                ClassicGame(
                                    time = mlsLong,
                                    date = System.currentTimeMillis(),
                                    win = win
                                )
                            )
                            Log.e("nnnn","8  progress-$progress   progressValue-$progressValue")
                            _finishGame.postValue(Pair(win,finishMessage))
                        }

                    }
                }
            }
        }
        downTimer.start()
    }


    fun insertNumber(num: Int) {
        _number.value = num
    }


    fun insertUserGameData(mistakes: Int) {
        Log.e("nnnn","2  mistakes-$mistakes")
        this.mistakes = mistakes
        downTimer.onFinish()
    }

    fun decreasedRemainNumbers(number:Int){
        sudokuNumbers.decreasedRemainNumbers(number)
    }

}