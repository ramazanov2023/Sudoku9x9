package com.example.sudoku9x9.ui.classic.finish

import android.os.Bundle
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.sudoku9x9.R
import com.example.sudoku9x9.SudokuApplication
import com.example.sudoku9x9.databinding.FragmentClassicFinishBinding

class ClassicFinishFragment: Fragment() {
    private lateinit var viewModel: ClassicFinishViewModel
    private lateinit var spanString: SpannableString
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentClassicFinishBinding.inflate(inflater)
        val args: ClassicFinishFragmentArgs by navArgs()
        val factory = ClassicFinishViewModelFactory((requireActivity().application as SudokuApplication).repository)
        viewModel = ViewModelProviders.of(this,factory).get(ClassicFinishViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val newText = args.finishMessage
        if(args.userWin){
            spanString = SpannableString(newText).apply {
                setSpan(TextAppearanceSpan(context, R.style.TextLargeThin),0,5,0)
                setSpan(TextAppearanceSpan(context, R.style.TextLargeBlack),5,9,0)
                setSpan(TextAppearanceSpan(context, R.style.TextLargeThin),10,14,0)
                setSpan(TextAppearanceSpan(context, R.style.TextLargeBlack),15,25,0)
                setSpan(TextAppearanceSpan(context, R.style.TextLargeThin),26,29,0)
                setSpan(TextAppearanceSpan(context, R.style.TextLargeBlack),29,newText.length-1,0)
                setSpan(TextAppearanceSpan(context, R.style.TextLargeThin),newText.length-1,newText.length,0)
            }
        }else{
            binding.classicFinishBest.visibility = View.GONE
            spanString = SpannableString(newText).apply {
                setSpan(TextAppearanceSpan(context, R.style.TextLargeThin),0,5,0)
                setSpan(TextAppearanceSpan(context, R.style.TextLargeBlack),5,9,0)
                setSpan(TextAppearanceSpan(context, R.style.TextLargeThin),10,14,0)
                setSpan(TextAppearanceSpan(context, R.style.TextLargeBlack),15,25,0)
                setSpan(TextAppearanceSpan(context, R.style.TextLargeThin),26,29,0)
                setSpan(TextAppearanceSpan(context, R.style.TextLargeBlack),29,newText.length-1,0)
                setSpan(TextAppearanceSpan(context, R.style.TextLargeThin),newText.length-1,newText.length,0)
            }
        }

        binding.classicFinishMessage.text = spanString

        return binding.root
    }
}