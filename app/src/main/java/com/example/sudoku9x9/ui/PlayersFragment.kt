package com.example.sudoku9x9.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sudoku9x9.databinding.FragmentClassicStartBinding
import com.example.sudoku9x9.databinding.FragmentPlayersBinding

class PlayersFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPlayersBinding.inflate(inflater)
//        (activity as? AppCompatActivity)?.supportActionBar?.title = "Players"


        return binding.root
    }
}