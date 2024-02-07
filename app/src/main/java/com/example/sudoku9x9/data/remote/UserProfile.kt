package com.example.sudoku9x9.data.remote

data class UserProfile(
    val nickname:String = "",
    val userId:String = "",
    val email:String = "",
    val password:String = "",
    val avatar:String = "",
    val country:String = "",
    val gender:String = "",
    val signUp:Boolean = false,
    val signUpTime:Long = 0L,
    val proVersion:Boolean = false
)
