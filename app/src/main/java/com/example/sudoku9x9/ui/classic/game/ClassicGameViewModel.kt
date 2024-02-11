package com.example.sudoku9x9.ui.classic.game

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sudoku9x9.data.SudokuRepository
import com.example.sudoku9x9.data.local.ClassicCard
import com.example.sudoku9x9.data.local.ClassicGame
import com.example.sudoku9x9.ui.board.SudokuNumbersGenerator
import com.example.sudoku9x9.ui.classic.convertToTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClassicGameViewModel(private val repository: SudokuRepository, private val gameLevelId: Int) : ViewModel() {
    lateinit var downTimer: CountDownTimer
    var mistakes = 0
    private lateinit var listInputNumbers: MutableList<Boolean>

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

        downTimer = object : CountDownTimer(1800000, 10) {
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
                val mlsLong = mlsTime
                val endOfGame =
                    EndOfGame(time = mlsLong, mistakes = mistakes, gameLevelId = gameLevelId)
                Log.e("suged", "0 - endOfGame-$endOfGame")

                endOfGame.setUserGameEndData(object : EndOfGame.GameEndResult {
                    override fun setUserLastGamesTime(): Array<Long> {
                        return repository.getLastTenGameTime(gameLevelId)
                    }

                    override fun setUserRecords(): ClassicCard? {
                        return userRecords.value
                    }

                    override fun getNewUserRecords(records: ClassicCard, lastGame: ClassicGame) {
                        repository.updateClassicCardData(
                            games = records.games,
                            mistakes = records.mistakes,
                            lastMeanTime = records.lastMeanTime,
                            meanTime = records.meanTime,
                            lastTime = records.lastTime,
                            pastBesTime = records.pastBesTime,
                            bestTime = records.bestTime,
                            progress = records.progress,
                            progressValue = records.progressValue,
                            gameLevelId = records.id
                        )
                        repository.saveClassicGame(lastGame)
                    }

                    override fun getGameMessage(message: Pair<Boolean, String>) {
                        _finishGame.postValue(message)
                    }
                })
            }
        }
        downTimer.start()
    }

    fun insertNumber(num: Int) {
        makeInputNumberSelected(num - 1)
        _number.value = num
    }

    fun makeInputNumberSelected(num: Int) {
        _selectInputNumber.value = num
    }

    fun undo() {
        _undoNumber.value = true
    }

    fun removeUndo() {
        _undoNumber.value = null
    }

    fun insertUserGameData(mistakes: Int) {
        this.mistakes = mistakes
        downTimer.onFinish()
    }

    fun calculateRemainNumbers(action: Int, value: Int) {
        sudokuNumbers.decreasedRemainNumbers(action, value)
    }

    fun setSpeedMode() {
        _speedGameMode.value?.let {
            _speedGameMode.value = !it
        }
    }
}