package com.example.sudoku9x9.ui.battle.game

import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sudoku9x9.data.SudokuRepository
import com.example.sudoku9x9.ui.board.SudokuBoard
import com.example.sudoku9x9.ui.board.SudokuNumbersGenerator
import com.example.sudoku9x9.ui.board.convertToX

class BattleGameViewModel(private val repository: SudokuRepository, private val gameLevelId: Int) : ViewModel() {
//    lateinit var downTimer: CountDownTimer
    private lateinit var listInputNumbers: MutableList<Boolean>

    val userRecords = repository.getClassicGameUserData(gameLevelId)
    val sudokuNumbers = SudokuNumbersGenerator(gameLevelId)
    val sudokuBoard:SudokuBoard = SudokuBoard(level = gameLevelId)





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

    private val _visibleNumberButtons = MutableLiveData<List<Int>>()
    val visibleNumberButtons: LiveData<List<Int>>
        get() = _visibleNumberButtons

    private val _selectNumberButtons = MutableLiveData<Boolean>()
    val selectNumberButtons: LiveData<Boolean>
        get() = _selectNumberButtons

    private val _mistakes1 = MutableLiveData<String>()
    val mistakes1: LiveData<String>
        get() = _mistakes1

    private val _mistakes2 = MutableLiveData<String>()
    val mistakes2: LiveData<String>
        get() = _mistakes2

    private val _showBoard = MutableLiveData<Int>()
    val showBoard: LiveData<Int>
        get() = _showBoard


    init {
        startRandomPlayerGame(gameLevelId)
        sudokuNumbers.getShuffleNumbersList()
        _mistakes1.value = "---"
        _mistakes2.value = "---"
        _speedGameMode.value = false
        _selectInputNumber.value = 10
        _showBoard.value = View.VISIBLE
    }

    private fun startRandomPlayerGame(level: Int) {
        repository.getRemoteSudokuResource().startRandomPlayerGame(
            gameLevelId = level,
            onReceivedGameData = {},
            onGamePlay = { m1, m2, score1, score2, end ->
                Log.e("kkkk","1  - onGamePlay")
                _mistakes1.setValue(m1.convertToX())
                _mistakes2.setValue(m2.convertToX())
            },
            onGameStart = {
                _showBoard.setValue(View.GONE)
                turnOnTimer()
            })
    }

    fun setUserMistakes(mistakes:Int){
        repository.getRemoteSudokuResource().setUserMistakes(mistakes)
    }

    private fun turnOnTimer() {

        val downTimer = object : CountDownTimer(1800000, 10) {
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
//                // mlsTime продожает увеличиваться после вызова метоа onFinish
//                // поэтому закидываем ее в новую переменную
//                val mlsLong = mlsTime
//                val endOfGame =
//                    EndOfGame(time = mlsLong, mistakes = sudokuBoard.userMistakes, gameLevelId = gameLevelId)
//                Log.e("suged", "0 - endOfGame-$endOfGame")
//
//                endOfGame.setUserGameEndData(object : EndOfGame.GameEndResult {
//                    override fun setUserLastGamesTime(): Array<Long> {
//                        return repository.getLastTenGameTime(gameLevelId)
//                    }
//
//                    override fun setUserRecords(): ClassicCard? {
//                        return userRecords.value
//                    }
//
//                    override fun getNewUserRecords(records: ClassicCard, lastGame: ClassicGame) {
//                        repository.updateClassicCardData(
//                            games = records.games,
//                            mistakes = records.mistakes,
//                            lastMeanTime = records.lastMeanTime,
//                            meanTime = records.meanTime,
//                            lastTime = records.lastTime,
//                            pastBesTime = records.pastBesTime,
//                            bestTime = records.bestTime,
//                            progress = records.progress,
//                            progressValue = records.progressValue,
//                            gameLevelId = records.id
//                        )
//                        repository.saveClassicGame(lastGame)
//                    }
//
//                    override fun getGameMessage(message: Pair<Boolean, String>) {
//                        _finishGame.postValue(message)
//                    }
//                })
            }
        }
        downTimer.start()
    }
//
    fun insertNumber(num: Int) {
        makeInputNumberSelected(num - 1)
        _number.value = num
    }
//
    fun makeInputNumberSelected(num: Int) {
        _selectInputNumber.value = num
    }
//
    fun undo() {
        _undoNumber.value = true
    }

    fun removeUndo() {
        _undoNumber.value = null
    }

    fun insertUserGameData(mistakes: Int) {
//        this.mistakes = mistakes
//        downTimer.onFinish()
    }

    fun setSpeedMode() {
        _speedGameMode.value?.let {
            _speedGameMode.value = !it
        }
    }

    fun checkRemainedNumbers(remained: List<Int>) {
        _visibleNumberButtons.value = remained
    }


//
//    fun hideNumberButton(remained: List<Int>) {
//        _visibleNumberButtons.value = remained
//    }
}