package com.example.sudoku9x9.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class RemoteSudokuResource(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseAuth: FirebaseAuth
) {

    fun setUserSignIn(userEmail: String?, userPassword: String?, signIn: () -> Any) {
        if (userEmail == null || userPassword == null) return
        val user = firebaseAuth.currentUser ?: return
        val userSignIn = firebaseDatabase.reference.child("sudoku")
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
        val userSignIn = firebaseDatabase.reference.child("sudoku")
            .child("players")
            .child(userId)
            .child("profile")
            .child("signIn")

        userSignIn.setValue(false).addOnSuccessListener {
            signIn()
        }
    }

}