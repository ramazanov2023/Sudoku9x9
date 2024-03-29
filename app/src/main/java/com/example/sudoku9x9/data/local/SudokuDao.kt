package com.example.sudoku9x9.data.local

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface SudokuDao {

    // for Profile

    @Insert
    fun insertUserProfile(profile: Profile)

    @Query("SELECT * FROM profile_table WHERE id == :id")
    fun getUserProfile(id:Int):LiveData<Profile>

    @Query("SELECT * FROM profile_table WHERE id == :id")
    fun getProfile(id:Int):Profile

    @Query("SELECT firstLaunch FROM profile_table WHERE id == :id")
    fun checkFirstLaunch(id:Int):Boolean

    @Query("UPDATE profile_table SET userName = :userName,userEmail = :userEmail,userAvatar = :userAvatar WHERE id == :id")
    fun updateUserProfile(userName:String, userEmail:String, userAvatar:String, id:Int)

    @Query("UPDATE profile_table SET userAvatar = :userAvatar WHERE id == :id")
    fun updateUserAvatar(userAvatar:String, id:Int)

    @Query("UPDATE profile_table SET userId = :uid,userName = :nickname,userEmail = :email,userPassword = :password,signIn = :signIn,signUp = :signUp,signUpTime = :signUpTime,userCountry = :country  WHERE id == :id")
    fun saveRegistration(uid:String,nickname:String, email:String,password:String,signIn:Boolean,signUp:Boolean,signUpTime: Long,country: String, id:Int)

    @Query("UPDATE profile_table SET firstLaunch = :firstLaunch WHERE id == :id")
    fun setFirstLaunch(firstLaunch:Boolean, id:Int)

    @Query("UPDATE profile_table SET signIn = :userSignIn WHERE id == :id")
    fun setSignIn(userSignIn:Boolean, id:Int)

    @Query("UPDATE profile_table SET signIn = :signIn WHERE id == :id")
    fun setSignOut(signIn: Boolean, id: Int)






    // for Classic

    @Query("SELECT * FROM classic_card_table")
    fun getClassicCardsData():LiveData<List<ClassicCard>>

    @Query("SELECT * FROM classic_card_table WHERE id == :gameLevelId")
    fun getClassicGameUserData(gameLevelId:Int):LiveData<ClassicCard>

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

    @Query("UPDATE classic_card_table SET user1 = :user1,user2 = :user2,user3 = :user3,user4 = :user4, user5 = :user5,user6 = :user6,user7 = :user7,user8 = :user8,user9 = :user9 WHERE id == :gameLevelId")
    fun updateClassicCardPlayersData(
        user1:String,
        user2:String,
        user3:String,
        user4:String,
        user5:String,
        user6:String,
        user7:String,
        user8:String,
        user9:String,
        gameLevelId: Int)


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