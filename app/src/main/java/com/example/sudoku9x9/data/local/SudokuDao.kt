package com.example.sudoku9x9.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface SudokuDao {

    // for Classic

    @Query("SELECT * FROM classic_card_table")
    fun getClassicCardsData():LiveData<List<ClassicCard>>

    @Query("SELECT * FROM classic_card_table WHERE id == :gameLevelId")
    fun getClassicGameUserData(gameLevelId:Int):LiveData<ClassicCard>

    @Insert
    fun insertUserProfile(profile: Profile)

    @Query("SELECT * FROM profile_table WHERE id == :id")
    fun getUserProfile(id:Int):LiveData<Profile>

    @Query("SELECT * FROM profile_table WHERE id == :id")
    fun getProfile(id:Int):Profile

    @Query("SELECT firstLaunch FROM profile_table WHERE id == :id")
    fun checkFirstLaunch(id:Int):Boolean

    @Query("UPDATE classic_card_table SET lastMeanTime = :lastMeanTime,meanTime = :meanTime,lastTime = :lastTime,pastBesTime = :pastBesTime, bestTime = :bestTime,progress = :progress,progressValue = :progressValue,games = :games, mistakes = :mistakes WHERE id == :gameLevelId")
    fun updateClassicCardData(
        games:Long,
        mistakes: Int,
        lastMeanTime:Long,
        meanTime:Long,
        pastBesTime:Long,
        bestTime:Long,
        progress:Boolean,
        progressValue:Long,
        lastTime:Long,
        gameLevelId:Int)

    @Query("UPDATE profile_table SET userName = :userName,userEmail = :userEmail,userAvatar = :userAvatar WHERE id == :id")
    fun updateUserProfile(userName:String, userEmail:String, userAvatar:String, id:Int)

    @Query("UPDATE profile_table SET userId = :uid,userEmail = :email,userPassword = :password,signUpTime = :time  WHERE id == :id")
    fun saveRegistration(uid:String, email: String, password: String, time: Long, id:Int)

    @Query("UPDATE profile_table SET firstLaunch = :firstLaunch WHERE id == :id")
    fun setFirstLaunch(firstLaunch:Boolean, id:Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertClassicCardsData(vararg card:ClassicCard)




    // для ClassicGamesHistory

    @Insert
    fun insertClassicGame(game:ClassicGame)

    @Query("SELECT time FROM classic_games_history_table ORDER BY id DESC LIMIT 0,10")
    fun getLastTenClassicGames():Array<Long>

    @Query("SELECT time FROM classic_games_history_table WHERE gameLevelId = :gameLevelId ORDER BY id DESC LIMIT 0,10")
    fun getLastTenClassicGames(gameLevelId: Int):Array<Long>



}