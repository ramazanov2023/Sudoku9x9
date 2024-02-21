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

    private fun startRandomPlayerGame(level: Int) {
        repository.getRemoteSudokuResource().startRandomPlayerGame(
            gameLevelId = level,
            onReceivedGameData = {},
            onGamePlay = {mistakes1,mistakes2,score1,score2,end ->
                Log.e("kkkk","1  - onGamePlay")
                findNavController().navigate(GameFragmentDirections.actionGameFragmentToBattleGameFragment(level))
            }
        )
    }


    private fun selectGameLevel() {
        val select = ClassicLevelSelectionFragment()
        select.show(parentFragmentManager, TAG)
        parentFragmentManager.setFragmentResultListener(
            REQUEST_KEY,
            viewLifecycleOwner
        ) { _, result ->
            val level = result.getInt(ClassicLevelSelectionFragment.KEY_RESPONSE)
            startRandomPlayerGame(level)
            select.dismiss()
        }
    }


}