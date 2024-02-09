package com.example.sudoku9x9.ui.classic.start

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sudoku9x9.R
import com.example.sudoku9x9.SudokuApplication
import com.example.sudoku9x9.databinding.FragmentClassicStartBinding
import com.example.sudoku9x9.ui.GameFragmentDirections
import com.example.sudoku9x9.ui.classic.start.ClassicLevelSelectionFragment.Companion.KEY_RESPONSE

class ClassicStartFragment : Fragment() {
    private val viewModel by viewModels<ClassicStartViewModel> {
        ClassicStartViewModelFactory((requireActivity().application as SudokuApplication).repository)
    }
    private lateinit var binding: FragmentClassicStartBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClassicStartBinding.inflate(inflater)


        binding.classicStartRecycler.adapter = ClassicStartAdapter()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        /*minAnimMode = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
        minModeScale = AnimationUtils.loadAnimation(requireContext(), R.anim.mode_scale_min)
        minAnimMode.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.apply{
                    classicStartModeLastName.visibility = View.INVISIBLE
                    classicModeDescription.visibility = View.INVISIBLE
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
        maxAnimMode = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        maxModeScale = AnimationUtils.loadAnimation(requireContext(), R.anim.mode_scale_max)
        maxAnimMode.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.apply{
                    classicStartModeLastName.visibility = View.VISIBLE
                    classicModeDescription.visibility = View.VISIBLE
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })

        binding.classicStartModeFirstName.setOnClickListener {
            it.startAnimation(minAnimMode)
        }*/

        binding.classicStartRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var scrollPosition = 0
//            var scroll = 0
//            var run:Boolean = true
            var run:Boolean = viewModel.scroll

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val scroll = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//                scrollPosition += dy
//                Log.e("iiii", "0  -  scrollPosition-${scrollPosition}   scroll-${scroll}")
                if(scroll<1){
                    if(!run) {
                        binding.apply {
                            Log.e("iiii", "3  -  scroll-${scroll}")
                            classicModeContainer.setLongMode()
                        }
                        run = true
                        viewModel.scroll = true
//                        Log.e("iiii", "2  -  dy-${dy}")
                    }
                }else{
                    if(run) {
                        binding.apply{
                            Log.e("iiii", "4  -  scroll-${scroll}")
                            classicModeContainer.setShortMode()
                        }
                        run = false
                        viewModel.scroll = false
//                        Log.e("iiii", "1  -  dy-${dy}")
                    }
                }

                /*if (scrollPosition > 600) {
                    if(run) {
                        binding.apply{
                            classicModeContainer.setShortMode()
//                            classicStartModeLastName.startAnimation(minAnimMode)
//                            classicModeDescription.startAnimation(minAnimMode)
                        }
                        run = false

                        Log.e("iiii", "1  -  dy-${dy}")
                    }
                } else if (scrollPosition < 300) {
                    if(!run) {
                        binding.apply{
                            classicModeContainer.setLongMode()
//                            classicStartModeLastName.startAnimation(maxAnimMode)
//                            classicModeDescription.startAnimation(maxAnimMode)
                        }
                        run = true
                        Log.e("iiii", "2  -  dy-${dy}")
                    }
                }*/
            }
        })


        viewModel.start.observe(viewLifecycleOwner, Observer {
            Log.e("wwww", "2")
            it?.let {
                Log.e("wwww", "3")
//                findNavController().navigate(GameFragmentDirections.actionGameFragmentToClassicGameFragment(1))
                val dialog = ClassicLevelSelectionFragment()
                dialog.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.ClassicSelectionDialog)
                dialog.show(parentFragmentManager, ClassicLevelSelectionFragment.TAG)
                parentFragmentManager.setFragmentResultListener(ClassicLevelSelectionFragment.REQUEST_KEY,
                    viewLifecycleOwner,
                    FragmentResultListener { _, result ->
                        Log.e(
                            "nnnn",
                            "0.1   result.getInt(KEY_RESPONSE)-${result.getInt(KEY_RESPONSE)}"
                        )
                        findNavController().navigate(
                            GameFragmentDirections.actionGameFragmentToClassicGameFragment(
                                result.getInt(KEY_RESPONSE)
                            )
                        )
                        dialog.dismiss()
                    })
                viewModel.removeStart()
            }
        })


        binding.classicModeContainer.setListener {
            when(it){
                ActionModeBar.START_GAME ->{
                    viewModel.startGame()
                }
                ActionModeBar.CHANGE_MODE ->{

                }
            }
        }


        return binding.root
    }


}