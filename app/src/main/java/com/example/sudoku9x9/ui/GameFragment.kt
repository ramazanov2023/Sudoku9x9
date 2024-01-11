package com.example.sudoku9x9.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sudoku9x9.databinding.FragmentGameBinding
import com.google.android.material.tabs.TabLayoutMediator

class GameFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGameBinding.inflate(inflater)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Sudoku 9x9"


        binding.gameViewPager2.adapter = GamePager(childFragmentManager,lifecycle)

        TabLayoutMediator(binding.gameTabLayout,binding.gameViewPager2){
            tab,position ->
            when(position){
                0 -> tab.text = "Classic"
                1 -> tab.text = "Battle"
                2 -> tab.text = "Tourney"
            }
        }.attach()

        return binding.root
    }
}