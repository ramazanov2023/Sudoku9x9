package com.example.sudoku9x9.ui.classic.game

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sudoku9x9.data.SudokuRepository
import com.example.sudoku9x9.data.local.ClassicGame
import com.example.sudoku9x9.ui.board.SudokuNumbersGenerator
import com.example.sudoku9x9.ui.classic.convertToTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClassicGameViewModel(private val repository: SudokuRepository, private val gameLevelId: Int) :
    ViewModel() {
    private lateinit var downTimer: CountDownTimer
    var mistakes = 0
    private lateinit var listInputNumbers:MutableList<Boolean>

    val userRecords = repository.getClassicGameUserData(gameLevelId)
    val sudokuNumbers = SudokuNumbersGenerator(gameLevelId)

    private val _number = MutableLiveData<Int>()
    val number: LiveData<Int>
        get() = _number

    private val _timer = MutableLiveData<String?>()
    val timer: LiveData<String?>
        get() = _timer

    private val _finishGame = MutableLiveData<Pair<Boolean, String>?>()
    val finishGame: LiveData<Pair<Boolean, String>?>
        get() = _finishGame

    private val _undoNumber = MutableLiveData<Boolean?>()
    val undoNumber: LiveData<Boolean?>
        get() = _undoNumber

    private val _speedGameMode = MutableLiveData<Boolean>()
    val speedGameMode: LiveData<Boolean>
        get() = _speedGameMode

    private val _selectInputNumber = MutableLiveData<Int?>()
    val selectInputNumber: LiveData<Int?>
        get() = _selectInputNumber


    init {
        sudokuNumbers.getShuffleNumbersList()
        turnOnTimer()
        _speedGameMode.value = false
        _selectInputNumber.value = 10
    }

    private fun turnOnTimer() {

        downTimer = object : CountDownTimer(600000, 10) {
            var min = 0
            var sec = 0
            var mls = 0
            var mlsTime: Long = 0
            override fun onTick(millisUntilFinished: Long) {
                mlsTime += 10L
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
                // mlsTime продожает увеличиваться после вызова метоа onFinish
                // поэтому закидываем ее в новую переменную
                var mlsLong = mlsTime
                Log.e("nnnn", "3  onFinish-$mistakes")
                viewModelScope.launch {
                    Log.e("nnnn", "4  viewModelScope")
                    var win = true
                    if (mistakes == 3) win = false
                    var lastMeanTime = 0L
                    if (win) {
                        mlsLong += mistakes * 2000
                        lastMeanTime = mlsLong
                    } else {
                        mlsLong = 0L
                    }
                    Log.e("nnnn", "5  win-$win   lastMeanTime-$lastMeanTime   mlsLong-$mlsLong")
                    withContext(Dispatchers.IO) {
                        var finishMessage = ""
                        Log.e("nnnn", "5.2  win-$win   gameLevelId-$gameLevelId   mlsLong-$mlsLong")
                        val lastTenGames = repository.getLastTenGameTime(gameLevelId)
                        Log.e(
                            "nnnn",
                            "5.3  win-$win   lastMeanTime-$lastMeanTime   mlsLong-$mlsLong"
                        )
                        if (lastTenGames.isNotEmpty()) {
                            lastTenGames.forEach {
                                lastMeanTime += it
                                Log.e("nnnn", "5.4  lastTenGames  lastMeanTime-$it")
                            }
                            lastMeanTime /= lastTenGames.size + 1
                        }

                        val lastGameData = userRecords.value

                        lastGameData?.let {
                            Log.e("nnnn", "6  lastMeanTime-$lastMeanTime   mlsLong-$mlsLong")
                            var bestTime = it.bestTime
                            if (win) {
                                bestTime = if(bestTime>0){
                                    Math.min(mlsLong, it.bestTime)
                                }else{
                                    mlsLong
                                }

                            } else {
                                lastMeanTime = if (it.lastMeanTime != 0L) {
                                    it.lastMeanTime * 10 / 100 + it.lastMeanTime
                                } else {
                                    0
                                }
                            }
                            var progress = true
                            var progressValue = 0L
                            if (lastMeanTime < it.lastMeanTime) {
                                progressValue = it.lastMeanTime - lastMeanTime
                                finishMessage =
                                    "Your mean time \ndecreased \nby ${progressValue.convertToTime()}s"
                            } else {
                                progress = false
                                progressValue = lastMeanTime - it.lastMeanTime
                                finishMessage =
                                    "Your mean time \nincreased \nby ${progressValue.convertToTime()}s"
                            }

                            Log.e("nnnn", "7  progress-$progress   finishMessage-$finishMessage")
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
                                gameLevelId = gameLevelId
                            )
                            repository.saveClassicGame(
                                ClassicGame(
                                    gameLevelId = gameLevelId,
                                    time = mlsLong,
                                    date = System.currentTimeMillis(),
                                    win = win
                                )
                            )
                            Log.e("nnnn", "8  progress-$progress   progressValue-$progressValue")
                            _finishGame.postValue(Pair(win, finishMessage))
                        }

                    }
                }
            }
        }
        downTimer.start()
    }


    fun insertNumber(num: Int) {
        makeInputNumberSelected(num-1)
        _number.value = num
    }

    fun makeInputNumberSelected(num: Int) {
        _selectInputNumber.value = num
    }

    fun undo(){
        _undoNumber.value = true
    }
    fun removeUndo(){
        _undoNumber.value = null
    }


    fun insertUserGameData(mistakes: Int) {
        Log.e("nnnn", "2  mistakes-$mistakes")
        this.mistakes = mistakes
        downTimer.onFinish()
    }

    fun calculateRemainNumbers(action: Int, value: Int) {
        sudokuNumbers.decreasedRemainNumbers(action,value)
    }

    fun setSpeedMode(){
        _speedGameMode.value?.let {
            _speedGameMode.value = !it
        }

    }

}