package com.example.sudoku9x9.ui.classic.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.sudoku9x9.SudokuApplication
import com.example.sudoku9x9.databinding.FragmentClassicGameBinding
import com.example.sudoku9x9.ui.board.INACTIVE_NUMBER
import com.example.sudoku9x9.ui.board.SudokuBoardView

class ClassicGameFragment: Fragment(),SudokuBoardView.SudokuListener {
    private lateinit var viewModel: ClassicGameViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentClassicGameBinding.inflate(inflater)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Fast Classic"

        val args:ClassicGameFragmentArgs by navArgs()
        val factory = ClassicGameViewModelFactory((requireActivity().application as SudokuApplication).repository,args.gameLevelId)
        viewModel = ViewModelProviders.of(this,factory).get(ClassicGameViewModel::class.java)

        binding.classicGameBoard.setListener(this)

        viewModel.sudokuNumbers.numbersLiveData.observe(viewLifecycleOwner, Observer {
            binding.classicGameBoard.insertSudokuNumbers(it)
        })

        viewModel.number.observe(viewLifecycleOwner, Observer {
            binding.classicGameBoard.checkInputNumber(it)
        })



        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        return binding.root
    }

    override fun action(id: Int, value: Int) {
        when(id){
            INACTIVE_NUMBER -> viewModel.addInactiveNumber(value)
        }
    }
}