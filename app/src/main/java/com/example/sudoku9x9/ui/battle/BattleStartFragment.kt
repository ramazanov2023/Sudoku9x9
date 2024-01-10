package com.example.sudoku9x9.ui.battle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sudoku9x9.databinding.FragmentBattleStartBinding
import com.example.sudoku9x9.databinding.FragmentClassicStartBinding

class BattleStartFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentBattleStartBinding.inflate(inflater)


        return binding.root
    }
}