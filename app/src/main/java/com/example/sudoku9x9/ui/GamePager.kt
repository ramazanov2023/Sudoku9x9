package com.example.sudoku9x9.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sudoku9x9.ui.battle.BattleStartFragment
import com.example.sudoku9x9.ui.classic.start.ClassicStartFragment
import com.example.sudoku9x9.ui.tourney.TourneyStartFragment

class GamePager(fragmentManager:FragmentManager, lifecycle:Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ClassicStartFragment()
            1 -> BattleStartFragment()
            2 -> TourneyStartFragment()
            else -> ClassicStartFragment()
        }
    }
}