package com.example.sudoku9x9.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.sudoku9x9.data.local.ClassicCard
import com.example.sudoku9x9.data.local.ClassicGame
import com.example.sudoku9x9.data.local.LocalSudokuResource
import com.example.sudoku9x9.data.local.Profile
import com.example.sudoku9x9.data.remote.RemoteSudokuResource
import com.example.sudoku9x9.data.remote.UserProfile
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DefaultSudokuRepository(
    private val localSudokuResource: LocalSudokuResource,
    private val remoteSudokuResource: RemoteSudokuResource
) : SudokuRepository {

    private var database: FirebaseDatabase =
        FirebaseDatabase.getInstance("https://sudoku9x9-276cf-default-rtdb.europe-west1.firebasedatabase.app/")

    override fun getRemoteDatabase():FirebaseDatabase{
        return database
    }

    override fun getClassicCardsData(): LiveData<List<ClassicCard>> {
        return localSudokuResource.sudokuDao.getClassicCardsData()
    }

    override fun updateUserData() {
        CoroutineScope(Dispatchers.Main).launch {
            Log.e("qqq", "0")
            withContext(Dispatchers.IO) {
                Log.e("qqq", "0.1")
                val check2 = localSudokuResource.sudokuDao
                Log.e("qqq", "0.2 - check-$check2")
                val check = localSudokuResource.sudokuDao.checkFirstLaunch(1)
                Log.e("qqq", "0.3 - check-$check")
                if (!check) {
                    Log.e("qqq", "1")
                    localSudokuResource.sudokuDao.insertClassicCardsData(*getCardsList().toTypedArray())
                    localSudokuResource.sudokuDao.insertUserProfile(Profile(id = 1))
                    localSudokuResource.sudokuDao.setFirstLaunch(true, 1)
                    Log.e("qqq", "2")
                } else {
                    getBestPlayers()
                    Log.e("qqq", "3.5  -  playersList")
                }
            }
        }
    }

    override fun setUserSignIn() {
        // Передаем значение signIn в Firebase
        CoroutineScope(Dispatchers.Main).launch {
            val userProfile:Profile?
            withContext(Dispatchers.IO) {
                userProfile = checkRegistration()
            }
            userProfile ?:return@launch

            // Проверяем, зарегистировался пользователь или нет
            if (userProfile.signUp) {
                remoteSudokuResource.setUserSignIn(
                    userProfile.userEmail,
                    userProfile.userPassword
                ) {
                    // Если запись добавлена успешно то передаем ее в локальную базу данных
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.IO) {
                            localSudokuResource.sudokuDao.setSignIn(true, 1)
                        }
                    }
                }
            }
        }
    }

    override fun setUserSignOut() {
        remoteSudokuResource.setUserSignOut {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    localSudokuResource.sudokuDao.setSignOut(false, 1)
                    Log.e("jjjj", "2.2  setUserSignOut")
                }
            }
        }
    }

    override fun updateClassicCardData(
        games: Long,
        mistakes: Int,
        lastMeanTime: Long,
        meanTime: Long,
        lastTime: Long,
        pastBesTime: Long,
        bestTime: Long,
        progress: Boolean,
        progressValue: Long,
        gameLevelId: Int
    ) {
        localSudokuResource.sudokuDao.updateClassicCardData(
            games = games,
            mistakes = mistakes,
            lastMeanTime = lastMeanTime,
            meanTime = meanTime,
            bestTime = bestTime,
            pastBesTime = pastBesTime,
            lastTime = lastTime,
            progress = progress,
            progressValue = progressValue,
            gameLevelId = gameLevelId
        )
    }

    override fun insertClassicCardsData(data: ClassicCard) {
        localSudokuResource.sudokuDao.insertClassicCardsData(data)
    }

    override fun getClassicGameUserData(gameLevelId: Int): LiveData<ClassicCard> {
        return localSudokuResource.sudokuDao.getClassicGameUserData(gameLevelId)
    }


    override fun getLastTenGameTime(gameLevelId: Int): Array<Long> {

        return localSudokuResource.sudokuDao.getLastTenClassicGames(gameLevelId)
//        return arrayOf(36700,40510,32590,47120,31568,36700,40510,32590,47120)
    }

    override fun saveClassicGame(classicGame: ClassicGame) {
        localSudokuResource.sudokuDao.insertClassicGame(classicGame)
    }

    override fun getUserProfile(id: Int): LiveData<Profile> {
        return localSudokuResource.sudokuDao.getUserProfile(id)
    }

    override fun checkRegistration(): Profile {
        return localSudokuResource.sudokuDao.getProfile(1)
    }

    override fun saveRegistration(profile: Profile) {
        Log.e(
            "search_null",
            "4  -  profile-$profile"
        )
        localSudokuResource.sudokuDao.saveRegistration(
            profile.userId!!,
            profile.userName!!,
            profile.userEmail!!,
            profile.userPassword!!,
            profile.signIn,
            profile.signUp,
            profile.signUpTime!!,
            profile.userCountry!!,
            profile.id
        )
    }

    override fun updateUserAvatar(userAvatar: String, id: Int) {
        localSudokuResource.sudokuDao.updateUserAvatar(userAvatar, id)
    }

    private fun getCardsList(): List<ClassicCard> {
        return listOf(
            ClassicCard(
                id = 1,
                mistakes = 0,
                level = "Fast",
                rating = "3289",
                lastMeanTime = 0,
                meanTime = 0,
                lastTime = 0,
                pastBesTime = 300000,
                bestTime = 300000,
                progress = false,
                progressValue = 0,
                games = 0,
                record = "48:57"
            ),
            ClassicCard(
                id = 2,
                mistakes = 0,
                level = "Light",
                rating = "2476",
                lastMeanTime = 0,
                meanTime = 0,
                lastTime = 0,
                pastBesTime = 300000,
                bestTime = 300000,
                progress = false,
                progressValue = 0,
                games = 0,
                record = "52:54"
            ),
            ClassicCard(
                id = 3,
                mistakes = 0,
                level = "Hard",
                rating = "1814",
                lastMeanTime = 0,
                meanTime = 0,
                lastTime = 0,
                pastBesTime = 300000,
                bestTime = 300000,
                progress = false,
                progressValue = 0,
                games = 0,
                record = "52:54"
            ),
            ClassicCard(
                id = 4,
                mistakes = 0,
                level = "Master",
                rating = "5932",
                lastMeanTime = 0,
                meanTime = 0,
                lastTime = 0,
                pastBesTime = 300000,
                bestTime = 300000,
                progress = false,
                progressValue = 0,
                games = 0,
                record = "52:54"
            )
        )
    }

    private fun getBestPlayers() {
        val list = mutableListOf<String>()
        Log.e("qqq", "4")
        val myRef = database.getReference().child("sudoku").child("players")
        Log.e("qqq", "4.2  -  myRef-$myRef")

        myRef.get().addOnCompleteListener {
            Log.e("qqq", "5")
            if (it.isSuccessful) {
                for (i in it.result.children) {
//                    val avatar = (i.child("profile").child("avatar").value) as String
                    val user = i.child("profile").getValue(UserProfile::class.java)
                    user ?: return@addOnCompleteListener
                    list.add(user.avatar)
                }
                setBestPlayers(list)
            }
        }
    }

    private fun setBestPlayers(list: List<String>) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                localSudokuResource.sudokuDao.updateClassicCardPlayersData(
                    list[0],
                    list[1],
                    list[2],
                    list[3],
                    list[4],
                    list[5],
                    list[6],
                    list[7],
                    list[8],
                    1
                )
                localSudokuResource.sudokuDao.updateClassicCardPlayersData(
                    list[2],
                    list[4],
                    list[1],
                    list[8],
                    list[0],
                    list[5],
                    list[6],
                    list[7],
                    list[3],
                    2
                )
                localSudokuResource.sudokuDao.updateClassicCardPlayersData(
                    list[3],
                    list[4],
                    list[2],
                    list[0],
                    list[1],
                    list[5],
                    list[8],
                    list[6],
                    list[7],
                    3
                )
                localSudokuResource.sudokuDao.updateClassicCardPlayersData(
                    list[6],
                    list[4],
                    list[2],
                    list[3],
                    list[1],
                    list[0],
                    list[8],
                    list[7],
                    list[5],
                    4
                )
            }
        }
    }


}