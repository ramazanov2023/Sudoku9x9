package com.example.sudoku9x9.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sudoku9x9.data.SudokuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(private val repository: SudokuRepository):ViewModel() {

    fun saveRegistration(uid:String,email:String,password:String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val time = System.currentTimeMillis()
                repository.saveRegistration(uid,email,password,time)
                Log.e("sasasa","5 - time-$time")
            }
        }
    }
}