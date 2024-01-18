package com.example.sudoku9x9.ui.classic.game

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sudoku9x9.data.SudokuRepository
import com.example.sudoku9x9.ui.board.SudokuNumbersGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Time
import java.util.*

class ClassicGameViewModel(private val repository: SudokuRepository,private val gameLevelId: Int) : ViewModel() {
    private lateinit var downTimer: CountDownTimer
    val userRecords = repository.getClassicGameUserData(gameLevelId)
    val sudokuNumbers = SudokuNumbersGenerator()

    private val _number = MutableLiveData<Int>()
    val number: LiveData<Int>
        get() = _number

    private val _inactiveNumber = MutableLiveData<Int?>()
    val inactiveNumber: LiveData<Int?>
        get() = _inactiveNumber

    private val _timer = MutableLiveData<String?>()
    val timer: LiveData<String?>
        get() = _timer




    init {
        sudokuNumbers.getShuffleNumbersList()
        turnOnTimer()
    }

    private fun turnOnTimer() {

        downTimer = object: CountDownTimer(600000, 100) {
            var min = 0
            var sec = 0
            var mls = 0
            override fun onTick(millisUntilFinished: Long) {
                mls++
                if(mls==10){
                    sec++
                    mls = 0
                }
                if(sec==60){
                    min++
                    sec = 0
                }
                if(min>0){
                    _timer.postValue("$min:$sec:$mls")
                }else{
                    _timer.postValue("$sec:$mls")
                }

            }

            override fun onFinish() {

            }
        }
        downTimer.start()
    }


    fun insertNumber(num:Int){
        _number.value = num
    }

    fun addInactiveNumber(num:Int){
        _inactiveNumber.value = num
    }

    fun insertUserGameData(mistakes:Int){
        downTimer.onFinish()
    }

}