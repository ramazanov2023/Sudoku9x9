package com.example.sudoku9x9.data.remote

import com.example.sudoku9x9.data.local.Profile

data class UserProfile(
    val nickname:String = "",
    val userId:String = "",
    val email:String = "",
    val password:String = "",
    val avatar:String = "",
    val country:String = "",
    val signIn:Boolean = false,
    val signUp:Boolean = false,
    val signUpTime:Long = 0L,
    val proVersion:Boolean = false
)

fun UserProfile.toProfile():Profile{
    return Profile(
        userId = this.userId,
        userName = this.nickname,
        userEmail = this.email,
        userPassword = this.password,
        signIn = this.signIn,
        signUp = this.signUp,
        signUpTime = this.signUpTime,
        userCountry = this.country,
        proVersion = this.proVersion,
        id = 1
    )
}
