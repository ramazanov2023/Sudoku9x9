package com.example.sudoku9x9.data

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.sudoku9x9.R
import com.example.sudoku9x9.data.local.ClassicCard
import com.example.sudoku9x9.data.local.ClassicGame
import com.example.sudoku9x9.data.local.LocalSudokuResource
import com.example.sudoku9x9.data.local.Profile
import com.example.sudoku9x9.data.remote.RemoteSudokuResource
import com.example.sudoku9x9.data.remote.UserProfile
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
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

    override fun getClassicCardsData(): LiveData<List<ClassicCard>> {
        return localSudokuResource.sudokuDao.getClassicCardsData()
    }

    override fun updateClassicCardsData() {
        CoroutineScope(Dispatchers.Main).launch {
            Log.e("qqq", "0")
            withContext(Dispatchers.IO) {
                val check = localSudokuResource.sudokuDao.checkFirstLaunch(1)
                if (!check) {
                    Log.e("qqq", "1")
                    localSudokuResource.sudokuDao.insertClassicCardsData(*getCardsList().toTypedArray())
                    localSudokuResource.sudokuDao.insertUserProfile(Profile(id = 1))
                    localSudokuResource.sudokuDao.setFirstLaunch(true, 1)
                    Log.e("qqq", "2")
                } else {
                    getBestPlayers()
//                    setBestPlayers(playersList)
                    Log.e("qqq", "3  -  playersList")
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

    override fun saveRegistration(
        uid: String,
        nickname: String,
        email: String,
        password: String,
        signUp: Boolean,
        time: Long,
        country: String
    ) {
        Log.e(
            "search_null",
            "4  -  nickname-$nickname  email-$email  password-$password  uid-${uid}"
        )
        localSudokuResource.sudokuDao.saveRegistration(
            uid,
            nickname,
            email,
            password,
            signUp,
            time,
            country,
            1
        )
    }

    override fun updateUserAvatar(userAvatar: String, id: Int) {
        localSudokuResource.sudokuDao.updateUserAvatar(userAvatar, id)
    }

    /*private fun getCardsList(): List<ClassicCard> {
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
                record = "48:57",
                user1 = R.drawable.fot_11,
                user2 = R.drawable.fot_1,
                user3 = R.drawable.fot_10,
                user4 = R.drawable.fot_14,
                user5 = R.drawable.fot_15,
                user6 = R.drawable.fot_8,
                user7 = R.drawable.fot_2,
                user8 = R.drawable.fot_5,
                user9 = R.drawable.fot_12,
                user10 = R.drawable.fot_16,
                user11 = R.drawable.fot_13,
                user12 = R.drawable.fot_17,
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
                record = "52:54",
                user1 = R.drawable.fot_5,
                user2 = R.drawable.fot_15,
                user3 = R.drawable.fot_10,
                user4 = R.drawable.fot_13,
                user5 = R.drawable.fot_1,
                user6 = R.drawable.fot_12,
                user7 = R.drawable.fot_2,
                user8 = R.drawable.fot_11,
                user9 = R.drawable.fot_17,
                user10 = R.drawable.fot_16,
                user11 = R.drawable.fot_14,
                user12 = R.drawable.fot_8,
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
                record = "52:54",
                user1 = R.drawable.fot_12,
                user2 = R.drawable.fot_13,
                user3 = R.drawable.fot_15,
                user4 = R.drawable.fot_17,
                user5 = R.drawable.fot_1,
                user6 = R.drawable.fot_14,
                user7 = R.drawable.fot_5,
                user8 = R.drawable.fot_11,
                user9 = R.drawable.fot_8,
                user10 = R.drawable.fot_16,
                user11 = R.drawable.fot_18,
                user12 = R.drawable.fot_10,
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
                record = "52:54",
                user1 = R.drawable.fot_1,
                user2 = R.drawable.fot_5,
                user3 = R.drawable.fot_19,
                user4 = R.drawable.fot_11,
                user5 = R.drawable.fot_2,
                user6 = R.drawable.fot_12,
                user7 = R.drawable.fot_15,
                user8 = R.drawable.fot_13,
                user9 = R.drawable.fot_8,
                user10 = R.drawable.fot_16,
                user11 = R.drawable.fot_14,
                user12 = R.drawable.fot_18,
            )
        )
    }*/

    private fun getCardsList(): List<ClassicCard> {
//       getBestPlayers()
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

    private fun getBestPlayers(){
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
                    /*for (i in 1..4) {
                        localSudokuResource.sudokuDao.updateClassicCardPlayersData(
                            list[0],
                            list[1],
                            list[2],
                            list[3],
                            list[4],
                            list[5],
                            i
                        )
                    }*/
                }
            }

//            Log.e("qqq", "6  -  list-${list.size}")


    }

    private fun setBestPlayers(list: List<String>) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO){
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