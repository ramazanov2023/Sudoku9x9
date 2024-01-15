package com.example.sudoku9x9.ui.classic.finish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sudoku9x9.databinding.FragmentClassicFinishBinding

class ClassicFinishFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentClassicFinishBinding.inflate(inflater)


        return binding.root
    }
}