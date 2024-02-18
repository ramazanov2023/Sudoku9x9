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


    // Проверка, есть в категории запросы запрос
    fun startRandomPlayerGame(gameLevelId:Int, onGameStart:()->Any){
        val battleRequest = database.reference.child("sudoku").child("battle_request_$gameLevelId")
        val battleGame = database.reference.child("sudoku").child("battle_games")

        battleRequest.get().addOnSuccessListener {
            if(it.hasChildren()){
                acceptGameRequest(it,battleRequest,battleGame,onGameStart)
            }else{
                createGameRequest(battleRequest,battleGame,onGameStart)
            }
        }
    }

    private fun acceptGameRequest(
        dataSnapshot: DataSnapshot,
        battleRequest: DatabaseReference,
        battleGame: DatabaseReference,
        onGameStart: () -> Any,
    ) {
        for(i in dataSnapshot.children){
            val r = i.getValue(BattleRequest::class.java) ?:return
            if (!r.accept){
                createGame(r,i.key!!,battleGame,battleRequest,onGameStart)
            }
        }
    }

    private fun createGame(
        requestData: BattleRequest,
        requestKey: String,
        battleGame: DatabaseReference,
        battleRequest: DatabaseReference,
        onGameStart: () -> Any
    ) {
        val gameRef = battleGame.push()
        gameRef.setValue(BattleGame()).addOnSuccessListener {
            Log.e("xxxx", "8  -  battleGame-$battleGame")

            gameRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val result = snapshot.getValue(BattleGame::class.java) ?: return
                    if (result.gameStart) {
                        Log.e("xxxx", "11  -  result-$result")
                        onGameStart.invoke()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

            battleRequest.child(requestKey).child("accept").setValue(true)
            battleRequest.child(requestKey).child("gameId").setValue(gameRef.key)

        }
    }

    private fun createGameRequest(
        battleRequest: DatabaseReference,
        battleGame: DatabaseReference,
        onGameStart: () -> Any
    ) {
        val request = BattleRequest(sudokuNumbers = "1_9h_3_5_6h_2_3_4_5h_8h_1_3_5_6h_9_4h_5_7_6")
        val requestRef = database.reference.child("sudoku").child("battle_request").push()
        requestRef.setValue(request).addOnSuccessListener {
            requestRef.addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val result = snapshot.getValue(BattleRequest::class.java) ?:return
                    if(result.accept){
                        val gameId = result.gameId ?:return
                        battleGame.child(gameId).child("gameStart").setValue(true).addOnSuccessListener {
                            onGameStart.invoke()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
    }


}