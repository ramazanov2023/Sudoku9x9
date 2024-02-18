package com.example.sudoku9x9.ui.battle

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.sudoku9x9.SudokuApplication
import com.example.sudoku9x9.data.SudokuRepository
import com.example.sudoku9x9.data.remote.BattleGame
import com.example.sudoku9x9.data.remote.BattleRequest
import com.example.sudoku9x9.databinding.FragmentBattleStartBinding
import com.example.sudoku9x9.databinding.FragmentClassicStartBinding
import com.example.sudoku9x9.ui.GameFragmentDirections
import com.example.sudoku9x9.ui.classic.start.ClassicLevelSelectionFragment
import com.example.sudoku9x9.ui.classic.start.ClassicLevelSelectionFragment.Companion.REQUEST_KEY
import com.example.sudoku9x9.ui.classic.start.ClassicLevelSelectionFragment.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BattleStartFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var repository: SudokuRepository
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentBattleStartBinding.inflate(inflater)
        repository = (requireActivity().application as SudokuApplication).repository
        auth = FirebaseAuth.getInstance()
        // Пользователь заходит в экран Battle
        // Нажимает RandomPlayer
        // Открывается диалоговое окно с выбором уровня
        binding.battlePlayRandomPlayer.setOnClickListener {
            selectGameLevel()
        }
        // После нажатия на уровень диалоговое окно закрывается и открывается окно с поиском(определением) соперника и загрузкой игры
        // После того как соперник определися, пользователь видит его аватар, имя, рейтинг

        return binding.root
    }

    private fun createGameRequest(repository: SudokuRepository) {
        val database = repository.getRemoteDatabase()
        Log.e("xxxx", "0  -  database-$database")
        database.reference.child("sudoku").child("battle_request")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    Log.e("xxxx", "1  -  task-$task")

                    if (task.result.hasChildren()) {
                        val count = task.result.children.count()

                        Log.e("xxxx", "5  -  count-$count")

                        val req = task.result.children.first()


                        Log.e("xxxx", "6  -  req-$req")

                        val reqResult = req.getValue(BattleRequest::class.java)

                        Log.e("xxxx", "7  -  reqResult-$reqResult")
                        // Создаем игру(сеанс)
                        val game = BattleGame()
                        val gameRef =
                            database.reference.child("sudoku").child("battle_games").push()
                        gameRef.setValue(game).addOnSuccessListener {
                            Log.e("xxxx", "8  -  game-$game")
                            gameRef.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val req = snapshot.getValue(BattleGame::class.java) ?: return
                                    if (req.gameStart) {
                                        Log.e("xxxx", "11  -  req-$req")
//                                        findNavController().navigate(GameFragmentDirections.actionGameFragmentToBattleGameFragment(1))
                                        Log.e("xxxx", "12  -  req-$req")
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }
                            })
                        }
                        // Принимаем запрос(меняем значение поля "accept" на true)
                        val reqRef = database.reference.child("sudoku").child("battle_request")
                            .child(req.key!!)
                        reqRef.child("accept").setValue(true).addOnSuccessListener {
                            Log.e("xxxx", "9  -  game-$game")
                        }
                        reqRef.child("gameId").setValue(gameRef.key).addOnSuccessListener {
                            Log.e("xxxx", "9.1  -  game-$game")
                        }

                    } else {
                        Log.e("xxxx", "3")
                        val user = auth.currentUser ?: return@addOnCompleteListener
                        val request = BattleRequest(
                            sudokuNumbers = "1235623458135624566"
                        )

                        val refRequest =
                            database.reference.child("sudoku").child("battle_request")
                                .push()
                        refRequest.setValue(request)
                            .addOnSuccessListener {
                                Log.e("xxxx", "4")
                            }
                        refRequest.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                val req = snapshot.getValue(BattleRequest::class.java) ?: return
                                if (req.accept) {
                                    val gameId = req.gameId
                                    gameId ?: return
                                    Log.e("xxxx", "10  -  gameId-$gameId")
                                    database.reference.child("sudoku").child("battle_games")
                                        .child(gameId).child("gameStart").setValue(true)
                                        .addOnSuccessListener {
                                            Log.e("xxxx", "13  -  req-$req")
                                            findNavController().navigate(
                                                GameFragmentDirections.actionGameFragmentToBattleGameFragment(
                                                    1
                                                )
                                            )
                                            Log.e("xxxx", "14  -  req-$req")
                                        }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("xxxx", "11  -  error-$error")
                            }
                        })
                    }
                }
            }
    }

    private fun selectGameLevel() {
        val select = ClassicLevelSelectionFragment()
        select.show(parentFragmentManager, TAG)
        parentFragmentManager.setFragmentResultListener(
            REQUEST_KEY,
            viewLifecycleOwner
        ) { _, result ->
            val level = result.getInt(ClassicLevelSelectionFragment.KEY_RESPONSE)
            createGameRequest(repository)
            select.dismiss()
        }
    }


}