package com.example.sudoku9x9.ui.classic.start

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.sudoku9x9.R
import com.example.sudoku9x9.SudokuApplication
import com.example.sudoku9x9.databinding.FragmentClassicStartBinding
import com.example.sudoku9x9.ui.GameFragmentDirections
import com.example.sudoku9x9.ui.classic.start.ClassicLevelSelectionFragment.Companion.KEY_RESPONSE

class ClassicStartFragment: Fragment() {
    private val viewModel by viewModels<ClassicStartViewModel> {
        ClassicStartViewModelFactory((requireActivity().application as SudokuApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentClassicStartBinding.inflate(inflater)
        Log.e("wwww","1")


        binding.classicStartRecycler.adapter = ClassicStartAdapter()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.start.observe(viewLifecycleOwner, Observer {
            Log.e("wwww","2")
            it?.let {
                Log.e("wwww","3")
//                findNavController().navigate(GameFragmentDirections.actionGameFragmentToClassicGameFragment(1))
                val dialog = ClassicLevelSelectionFragment()
                dialog.setStyle(DialogFragment.STYLE_NO_FRAME,R.style.ClassicSelectionDialog)
                dialog.show(parentFragmentManager,ClassicLevelSelectionFragment.TAG)
                parentFragmentManager.setFragmentResultListener(ClassicLevelSelectionFragment.REQUEST_KEY,viewLifecycleOwner,
                FragmentResultListener{
                    _,result ->
                    Log.e("nnnn","0.1   result.getInt(KEY_RESPONSE)-${result.getInt(KEY_RESPONSE)}")
                    findNavController().navigate(GameFragmentDirections.actionGameFragmentToClassicGameFragment(result.getInt(KEY_RESPONSE)))
                    dialog.dismiss()
                })
                viewModel.removeStart()
            }
        })



        return binding.root
    }

}