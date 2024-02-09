package com.example.sudoku9x9.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

const val FAST_LEVEL = 1

@Entity(tableName = "classic_card_table")
data class ClassicCard(

    @PrimaryKey
    val id:Int,
    val level:String,
    val rating:String,
    val meanTime:Long,
    val lastMeanTime:Long,
    val pastBesTime:Long,
    val bestTime:Long,
    val progress:Boolean,
    val progressValue:Long,
    val lastTime:Long,
    val games:Long,
    val mistakes:Int,
    val record:String,
    val user1:String? = null,
    val user2:String? = null,
    val user3:String? = null,
    val user4:String? = null,
    val user5:String? = null,
    val user6:String? = null,
    val user7:String? = null,
    val user8:String? = null,
    val user9:String? = null,
    val user10:String? = null,
    val user11:String? = null,
    val user12:String? = null
)

@Entity(tableName = "profile_table")
data class Profile(
    @PrimaryKey
    val id:Int,
    var userName:String? = null,
    var userId:Int? = null,
    var userEmail:String? = null,
    var userPassword:String? = null,
    var userAvatar:String? = "",
    var userCountry:String? = null,
    var userGender:String? = null,
    var signUp:Boolean = false,
    var signUpTime:Long? = null,
    var proVersion:Boolean = false,
    var firstLaunch:Boolean = false
)

@Entity(tableName = "classic_games_history_table")
data class ClassicGame(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    var gameLevelId:Int,
    val time:Long,
    val win:Boolean,
    val date:Long
)