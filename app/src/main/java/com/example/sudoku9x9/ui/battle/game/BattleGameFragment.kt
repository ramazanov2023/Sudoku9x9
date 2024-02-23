package com.example.sudoku9x9.ui.battle.game

import android.content.Context
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
import com.example.sudoku9x9.databinding.FragmentBattleGameBinding
import com.example.sudoku9x9.ui.GameFragmentDirections
import com.example.sudoku9x9.ui.board.*

class BattleGameFragment : Fragment() {
    private lateinit var viewModel: BattleGameViewModel
    private lateinit var binding: FragmentBattleGameBinding
    private lateinit var titleSpan: SpannableString
    private val args: BattleGameFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBattleGameBinding.inflate(inflater)
//        args:BattleGameFragmentArgs by navArgs()
        val toolbarTitle = when (args.gameLevelId) {
            1 -> "Fast Battle"
            2 -> "Light Battle"
            3 -> "Hard Battle"
            4 -> "Master Battle"
            else -> "Fast Battle"
        }
        Log.e("nnnn", "0   args.gameLevelId-${args.gameLevelId} toolbarTitle-$toolbarTitle")

        titleSpan = SpannableString(toolbarTitle).apply {
            setSpan(TextAppearanceSpan(context, R.style.TextToolbarBold), 0, 4, 0)
            setSpan(TextAppearanceSpan(context, R.style.TextToolbarThin), 5, toolbarTitle.length, 0)
        }

        val factory = BattleGameViewModelFactory(
            (requireActivity().application as SudokuApplication).repository,
            args.gameLevelId
        )
        viewModel = ViewModelProviders.of(this, factory).get(BattleGameViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.sudokuNumbers.numbersLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.sudokuBoard.sudokuNumbers = it
        })

        viewModel.sudokuNumbers.remainNumbersLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.sudokuBoard.remainNumbers = it
                viewModel.checkRemainedNumbers(it)
            }
        })

        viewModel.number.observe(viewLifecycleOwner, Observer {
            binding.battleGameBoard.checkInputNumber(it)
        })

        viewModel.undoNumber.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.battleGameBoard.undo()
                viewModel.removeUndo()
            }
        })

        viewModel.speedGameMode.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.battleGameSpeedBtn.setColorFilter(Color.parseColor("#28B5FE"))
                binding.classicGameSpeedName.setTextColor(Color.parseColor("#28B5FE"))
            } else {
                binding.battleGameSpeedBtn.setColorFilter(Color.parseColor("#B6C0C6"))
                binding.classicGameSpeedName.setTextColor(Color.parseColor("#B6C0C6"))
            }
            binding.battleGameBoard.setSpeedMode(it)

        })

        setSudokuBoard()


        return binding.root
    }


    override fun onResume() {
        super.onResume()
        (activity as? AppCompatActivity)?.supportActionBar?.title = titleSpan
    }

    override fun onDestroy() {
        super.onDestroy()
//        viewModel.downTimer.cancel()
    }


    private fun setSudokuBoard() {
        binding.battleGameBoard.setSudokuBoard(viewModel.sudokuBoard,
            object : SudokuBoardView2.SudokuListener {

                override fun onInputRightNumber(remained: List<Int>, value: Int) {
                    viewModel.checkRemainedNumbers(remained)
                }

                override fun onInputWrongNumber(mistakes: Int) {
                    val vibrator: Vibrator =
                        (requireActivity().getSystemService(Context.VIBRATOR_SERVICE)) as Vibrator
                    vibrator.vibrate(70)
//                    binding.battleGameUserMistakes.text = mistakes.convertToX()
                    viewModel.setUserMistakes(mistakes)
                    viewModel.makeInputNumberSelected(NO_NUMBER_BUTTON)
                }

                override fun onSelectNumberCell(number: Int) {
                    viewModel.makeInputNumberSelected(number - 1)
                }

                override fun onSelectEmptyCell(id: Int) {
                    viewModel.makeInputNumberSelected(NO_NUMBER_BUTTON)
                }

                override fun onSelectCompleteValueCells(remained: List<Int>, id: Int) {
                    viewModel.makeInputNumberSelected(NO_NUMBER_BUTTON)
                }

                override fun onCancelUserStep(remained: Int, i: Int) {
                }

                override fun onGameEnd(mistakes: Int) {
//                viewModel.insertUserGameData(mistakes)
                }

            })
    }
}