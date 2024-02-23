package com.example.sudoku9x9.ui.battle

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sudoku9x9.R
import com.example.sudoku9x9.SudokuApplication
import com.example.sudoku9x9.data.SudokuRepository
import com.example.sudoku9x9.databinding.FragmentBattleStartBinding
import com.example.sudoku9x9.ui.GameFragmentDirections
import com.example.sudoku9x9.ui.classic.start.ClassicLevelSelectionFragment
import com.example.sudoku9x9.ui.classic.start.ClassicLevelSelectionFragment.Companion.REQUEST_KEY
import com.example.sudoku9x9.ui.classic.start.ClassicLevelSelectionFragment.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BattleStartFragment : Fragment() {
    private lateinit var repository: SudokuRepository
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentBattleStartBinding.inflate(inflater)
        repository = (requireActivity().application as SudokuApplication).repository
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


    private fun selectGameLevel() {
        val select = ClassicLevelSelectionFragment()
        select.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.ClassicSelectionDialog)
        select.show(parentFragmentManager, TAG)
        parentFragmentManager.setFragmentResultListener(
            REQUEST_KEY,
            viewLifecycleOwner
        ) { _, result ->
            val level = result.getInt(ClassicLevelSelectionFragment.KEY_RESPONSE)
            findNavController().navigate(GameFragmentDirections.actionGameFragmentToBattleGameFragment(level))
            select.dismiss()
        }
    }


}