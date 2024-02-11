package com.example.sudoku9x9.data

import androidx.lifecycle.LiveData
import com.example.sudoku9x9.data.local.ClassicCard
import com.example.sudoku9x9.data.local.ClassicGame
import com.example.sudoku9x9.data.local.Profile

interface SudokuRepository {

    fun getClassicCardsData(): LiveData<List<ClassicCard>>

    fun updateUserData()

    fun setUserSignOut()

    fun setUserSignIn()

    fun getClassicGameUserData(gameLevelId:Int): LiveData<ClassicCard>

    fun getLastTenGameTime(gameLevelId: Int):Array<Long>

    fun insertClassicCardsData(data:ClassicCard)

    fun updateClassicCardData(games: Long,
                              mistakes: Int,
                              lastMeanTime: Long,
                              meanTime: Long,
                              lastTime: Long,
                              pastBesTime: Long,
                              bestTime: Long,
                              progress:Boolean,
                              progressValue:Long,
                              gameLevelId: Int)

    fun saveClassicGame(classicGame: ClassicGame)




    fun getUserProfile(id:Int):LiveData<Profile>
    fun checkRegistration(): Profile
    fun saveRegistration(profile:Profile)
    fun updateUserAvatar(userAvatar:String, id:Int)


}