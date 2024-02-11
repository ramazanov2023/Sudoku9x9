package com.example.sudoku9x9.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sudoku9x9.data.SudokuRepository
import com.example.sudoku9x9.data.local.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(private val repository: SudokuRepository) : ViewModel() {

    var profileData = repository.getUserProfile(1)

    fun saveRegistration(profile: Profile) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.e("search_null","3  -  profile-$profile")
                repository.saveRegistration(profile)

            }
        }
    }

    fun updateUserProfile(userAvatar:String, id:Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.updateUserAvatar(userAvatar,id)
                Log.e("oooo", "2 - userAvatar-$userAvatar")
            }
        }
    }
}