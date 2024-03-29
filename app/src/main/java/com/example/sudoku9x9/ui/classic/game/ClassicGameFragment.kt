package com.example.sudoku9x9.ui.classic.game

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Vibrator
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sudoku9x9.R
import com.example.sudoku9x9.SudokuApplication
import com.example.sudoku9x9.databinding.FragmentClassicGameBinding
import com.example.sudoku9x9.ui.board.*

class ClassicGameFragment: Fragment(),SudokuBoardView.SudokuListener {
    private lateinit var viewModel: ClassicGameViewModel
    private lateinit var binding: FragmentClassicGameBinding
    private lateinit var titleSpan:SpannableString
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClassicGameBinding.inflate(inflater)
        val args:ClassicGameFragmentArgs by navArgs()
        val toolbarTitle = when(args.gameLevelId){
            1 -> "Fast Classic"
            2 -> "Light Classic"
            3 -> "Hard Classic"
            4 -> "Master Classic"
            else -> "Fast Classic"
        }
        Log.e("nnnn","0   args.gameLevelId-${args.gameLevelId} toolbarTitle-$toolbarTitle")

        titleSpan = SpannableString(toolbarTitle).apply {
            setSpan(TextAppearanceSpan(context, R.style.TextToolbarBold),0,4,0)
            setSpan(TextAppearanceSpan(context, R.style.TextToolbarThin),5,toolbarTitle.length,0)
        }

        binding.classicGameBoard.setLevel(args.gameLevelId)

        val factory = ClassicGameViewModelFactory((requireActivity().application as SudokuApplication).repository,args.gameLevelId)
        viewModel = ViewModelProviders.of(this,factory).get(ClassicGameViewModel::class.java)

        binding.classicGameBoard.setListener(this)

        viewModel.sudokuNumbers.numbersLiveData.observe(viewLifecycleOwner, Observer {
            binding.classicGameBoard.insertSudokuNumbers(it)
        })

        viewModel.sudokuNumbers.remainNumbersLiveData.observe(viewLifecycleOwner, Observer {
            binding.classicGameBoard.insertRemainNumbers(it)
        })

        viewModel.number.observe(viewLifecycleOwner, Observer {
            binding.classicGameBoard.checkInputNumber(it)
        })

        viewModel.undoNumber.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.classicGameBoard.undo()
                viewModel.removeUndo()
            }
        })

        viewModel.speedGameMode.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    binding.classicGameSpeedBtn.setColorFilter(Color.parseColor("#28B5FE"))
                    binding.classicGameSpeedName.setTextColor(Color.parseColor("#28B5FE"))
                }else{
                    binding.classicGameSpeedBtn.setColorFilter(Color.parseColor("#B6C0C6"))
                    binding.classicGameSpeedName.setTextColor(Color.parseColor("#B6C0C6"))
                }
                binding.classicGameBoard.setSpeedMode(it)
                viewModel.removeUndo()
            }
        })

        viewModel.finishGame.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(ClassicGameFragmentDirections
                    .actionClassicGameFragmentToClassicFinishFragment(it.first,it.second,args.gameLevelId))
            }

        })


        binding.viewModel = viewModel
        binding.lifecycleOwner = this



        return binding.root
    }

    override fun action(id: Int, value: Int) {
        when(id){
            USER_MISTAKES -> binding.classicGameUserMistakes.text = value.convertToX()
            SELECT_NUMBER_BUTTON -> {
                Log.e("nnnn","1  value-$value")
                viewModel.makeInputNumberSelected(value-1)
            }
            GAME_END -> {
                Log.e("nnnn","2  value-$value")
                viewModel.insertUserGameData(value)
            }
            REMAIN_NUMBERS_DECREASE -> {
                Log.e("bbbb","3  value-$value")
                viewModel.calculateRemainNumbers(id,value)
            }
            REMAIN_NUMBERS_INCREASE -> {
                Log.e("bbbb","4  value-$value")
                viewModel.calculateRemainNumbers(id,value)
            }
            MISTAKE_VIBRATE -> {
                Log.e("bbbb","5  value-$value")
                val vibrator:Vibrator = (requireActivity().getSystemService(Context.VIBRATOR_SERVICE)) as Vibrator
                vibrator.vibrate(70)

            }
//            GAME_END -> findNavController().navigate(R.id.classicFinishFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? AppCompatActivity)?.supportActionBar?.title = titleSpan
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.downTimer.cancel()
    }
}