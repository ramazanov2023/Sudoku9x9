package com.example.sudoku9x9.ui.classic.finish

import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.sudoku9x9.SudokuApplication
import com.example.sudoku9x9.databinding.FragmentClassicFinishBinding

class ClassicFinishFragment: Fragment() {
    private lateinit var viewModel: ClassicFinishViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentClassicFinishBinding.inflate(inflater)
        val factory = ClassicFinishViewModelFactory((requireActivity().application as SudokuApplication).repository)
        viewModel = ViewModelProviders.of(this,factory).get(ClassicFinishViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

//        val spanString = SpannableString("Your mean time decreased by 5s")


        return binding.root
    }
}