package com.example.sudoku9x9

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sudoku9x9.databinding.FragmentBottomNavigationBarBinding

class BottomNavigationBarFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentBottomNavigationBarBinding.inflate(inflater)



        return binding.root
    }
}