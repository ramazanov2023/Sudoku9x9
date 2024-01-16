package com.example.sudoku9x9.ui.classic.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.sudoku9x9.SudokuApplication
import com.example.sudoku9x9.databinding.FragmentClassicGameBinding

class ClassicGameFragment: Fragment() {
    /*private val viewModel by viewModels<ClassicGameViewModel> {
        ClassicGameViewModelFactory((requireActivity().application as SudokuApplication).repository)
    }*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentClassicGameBinding.inflate(inflater)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Fast Classic"

        val args:ClassicGameFragmentArgs by navArgs()
        val factory = ClassicGameViewModelFactory((requireActivity().application as SudokuApplication).repository,args.gameLevelId)
        val viewModel = ViewModelProviders.of(this,factory).get(ClassicGameViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        return binding.root
    }
}