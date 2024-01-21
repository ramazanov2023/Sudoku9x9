package com.example.sudoku9x9.ui

import android.os.Bundle
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sudoku9x9.R
import com.example.sudoku9x9.databinding.FragmentGameBinding
import com.google.android.material.tabs.TabLayoutMediator

class GameFragment: Fragment() {
    private lateinit var titleSpan:SpannableString
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGameBinding.inflate(inflater)
        val toolbarTitle = "Sudoku 9x9"
        titleSpan = SpannableString(toolbarTitle).apply {
            setSpan(TextAppearanceSpan(context, R.style.TextToolbarBold),0,6,0)
            setSpan(TextAppearanceSpan(context, R.style.TextToolbarThin),7,toolbarTitle.length,0)
        }



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

    override fun onResume() {
        super.onResume()
        (activity as? AppCompatActivity)?.supportActionBar?.title = titleSpan
    }
}