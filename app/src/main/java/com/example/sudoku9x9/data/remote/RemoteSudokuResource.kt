package com.example.sudoku9x9.data.remote

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class RemoteSudokuResource(
    private val database: FirebaseDatabase,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseAuth: FirebaseAuth
) {
    private var first: Boolean = false
    private var userId:String? = null
    private lateinit var battleRequest:DatabaseReference
    private lateinit var battleGame:DatabaseReference
    private var gameKey:String? = null

    init {
        userId = firebaseAuth.uid
        battleRequest = database.reference.child("sudoku").child("battle_request_1")
        battleGame = database.reference.child("sudoku").child("battle_games")
    }

    fun setUserSignIn(userEmail: String?, userPassword: String?, signIn: () -> Any) {
        if (userEmail == null || userPassword == null) return
        val user = firebaseAuth.currentUser ?: return
        val userSignIn = database.reference.child("sudoku")
            .child("players")
            .child(user.uid)
            .child("profile")
            .child("signIn")

        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    userSignIn.setValue(true).addOnSuccessListener {
                        signIn()
                    }
                }
            }
    }

    fun setUserSignOut(signIn: () -> Any) {
        val userId = firebaseAuth.uid ?: return
        val userSignIn = database.reference.child("sudoku")
            .child("players")
            .child(userId)
            .child("profile")
            .child("signIn")

        userSignIn.setValue(false).addOnSuccessListener {
            signIn()
        }
    }

    fun setUserMistakes(mistakes:Int){
        Log.e("xxxx", "12  -  mistakes-$mistakes")
        gameKey?.let {
            Log.e("xxxx", "13  -  mistakes-$mistakes")
            val user:String = if (first){
                "firstUserMistakes"
            }else {
                "secondUserMistakes"
            }
            Log.e("xxxx", "14  -  mistakes-$mistakes")
            battleGame.child(it).child(user).setValue(mistakes).addOnSuccessListener {
                Log.e("xxxx", "15  -  mistakes-$mistakes")
            }
        }
    }

    // Проверка, есть в категории запросы запрос
    fun startRandomPlayerGame(
        gameLevelId: Int,
        onGamePlay: (firstUserMistakes: Int, secondUserMistakes: Int, firstUserScore: Int, secondUserScore: Int, gameEnd: Boolean) -> Any,
        onReceivedGameData:(BattleGame) -> Any,onGameStart:()->Any
    ) {
        userId  ?:return
//        val battleRequest = database.reference.child("sudoku").child("battle_request_1")
//        val battleGame = database.reference.child("sudoku").child("battle_games")

        battleRequest.get().addOnSuccessListener {
            if (it.hasChildren()) {
                first = false
                acceptGameRequest(userId!!,battleGame, battleRequest, it,onReceivedGameData,onGamePlay,onGameStart)
            } else {
                first = true
                createGame(userId!!,battleGame, battleRequest, onGamePlay,onGameStart)
            }
        }
    }

    private fun createGame(
        userId: String,
        battleGame: DatabaseReference,
        battleRequest: DatabaseReference,
        onGamePlay: (firstUserMistakes:Int,
                     secondUserMistakes:Int,
                     firstUserScore:Int,
                     secondUserScore:Int,
                     gameEnd:Boolean) -> Any,
        onGameStart:()->Any
    ) {
        val gameRef = battleGame.push()
        gameKey = gameRef.key
        val game = BattleGame(
            sudokuNumbers = "1_9h_3_5_6h_2_3_4_5h_8h_1_3_5_6h_9_4h_5_7_6",
            firstUserId = userId
        )
        gameRef.setValue(game).addOnSuccessListener {
            Log.e("xxxx", "8  -  battleGame-$battleGame")
            gameRef.child("gameStart").addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val result = snapshot.value as Boolean
                    if (result){
                        onGameStart.invoke()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

            gameRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val result = snapshot.getValue(BattleGame::class.java) ?: return
                    if (result.gameStart) {
                        Log.e("xxxx", "11  -  result-$result")
                        onGamePlay(
                            result.firstUserMistakes,
                            result.secondUserMistakes,
                            result.firstUserScore,
                            result.secondUserScore,
                            result.gameEnd
                        )
                        Log.e("kkkk","3  - result-$result")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

            gameRef.key?.let { gameId -> createGameRequest(battleRequest, gameId) }
        }
    }

    private fun createGameRequest(battleRequest: DatabaseReference, gameId: String) {
        val requestRef = battleRequest.push()
        requestRef.child("gameId").setValue(gameId).addOnSuccessListener {
        }
    }

    private fun acceptGameRequest(
        userId: String,
        battleGame: DatabaseReference,
        battleRequest: DatabaseReference,
        requests: DataSnapshot,
        onReceivedGameData: (BattleGame) -> Any,
        onGamePlay: (firstUserMistakes:Int,
                     secondUserMistakes:Int,
                     firstUserScore:Int,
                     secondUserScore:Int,
                     gameEnd:Boolean) -> Any,
        onGameStart:()->Any
    ) {
        val req = requests.children.first()
        val gameId = req.child("gameId").value as String
        req.key?.let { requestId -> deleteGameRequest(battleRequest, requestId) }
        runGame(userId,battleGame, gameId,onReceivedGameData,onGamePlay,onGameStart)
    }

    private fun runGame(
        userId: String,
        battleGame: DatabaseReference,
        gameId: String,
        onReceivedGameData: (BattleGame) -> Any,
        onGamePlay: (firstUserMistakes:Int,
                     secondUserMistakes:Int,
                     firstUserScore:Int,
                     secondUserScore:Int,
                     gameEnd:Boolean) -> Any,
        onGameStart:()->Any
    ) {
        val gameDataRef = battleGame.child(gameId)
        gameDataRef.get().addOnSuccessListener {
            val gameData = it.getValue(BattleGame::class.java)
            gameData ?: return@addOnSuccessListener
            onReceivedGameData(gameData)
            gameData.apply {
                gameStart = true
                secondUserId = userId
            }
            gameDataRef.setValue(gameData).addOnSuccessListener {
                onGameStart.invoke()
            }
            gameDataRef.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val result = snapshot.getValue(BattleGame::class.java) ?: return
                    onGamePlay(
                        result.firstUserMistakes,
                        result.secondUserMistakes,
                        result.firstUserScore,
                        result.secondUserScore,
                        result.gameEnd
                    )
                    Log.e("kkkk","2  - result-$result")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    private fun deleteGameRequest(battleRequest: DatabaseReference, requestId: String) {
        battleRequest.child(requestId).removeValue().addOnSuccessListener { }
    }



}