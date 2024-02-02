package com.example.sudoku9x9.ui.classic.start

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.sudoku9x9.databinding.ClassicModeBarBinding

enum class ActionModeBar{
    START_GAME,
    CHANGE_MODE
}

typealias ModeBarListener = (ActionModeBar) -> Any

class ClassicModeBarView(context: Context,attr:AttributeSet):ConstraintLayout(context,attr) {

    private lateinit var animShortModeVersion:AnimatorSet
    private lateinit var animLongModeVersion:AnimatorSet
    private lateinit var binding:ClassicModeBarBinding
    private var listener:ModeBarListener? = null

    init {
        binding = ClassicModeBarBinding.inflate(LayoutInflater.from(context), this,true)
        binding.classicStartButtonPlay.setOnClickListener {
            listener?.invoke(ActionModeBar.START_GAME)
        }
//        initializeAnimator()

    }

    private fun initializeAnimator() {
        shortModeSet()
        longModeSet()


    }

    private fun longModeSet() {
//        animLongModeVersion = AnimatorSet()
        val titleSmall =
            ObjectAnimator.ofFloat(binding.classicStartModeFirstName, "textSize", 34F, 46f).apply {
                duration = 400
                interpolator = DecelerateInterpolator()
            }
        val short = ObjectAnimator.ofFloat(binding.classicTopFrame, "scaleY", 1f).apply {
            duration = 260
            interpolator = DecelerateInterpolator()
            // для scale необходимо установить pivot(в атрибутах или здесь,в коде)
        }
        val fadeLastNameMode =
            ObjectAnimator.ofFloat(binding.classicStartModeLastName, "alpha", 1f).apply {
                duration = 300
                interpolator = DecelerateInterpolator()
            }
        val fadeDescriptionMode =
            ObjectAnimator.ofFloat(binding.classicModeDescription, "alpha", 1f).apply {
                duration = 300
                interpolator = DecelerateInterpolator()
            }
        val fadeChangeButton =
            ObjectAnimator.ofFloat(binding.classicStartModeButton, "alpha", 1f).apply {
                duration = 300
                interpolator = DecelerateInterpolator()
            }
        val fadeChangeMode =
            ObjectAnimator.ofFloat(binding.classicStartModeChange, "alpha", 1f).apply {
                duration = 300
                interpolator = DecelerateInterpolator()
            }
        /*animShortModeVersion.play(titleSmall).with(short).with(fadeLastNameMode).with(fadeChangeMode)
            .with(fadeDescriptionMode).with(fadeChangeButton)*/

        AnimatorSet().apply {
            play(short).with(titleSmall).with(fadeLastNameMode).with(fadeChangeButton).with(fadeChangeMode).with(fadeDescriptionMode)
            start()
        }
    }

    private fun shortModeSet() {
//        animShortModeVersion = AnimatorSet()
        val titleSmall =
            ObjectAnimator.ofFloat(binding.classicStartModeFirstName, "textSize", 46F, 34f).apply {
                duration = 400
                interpolator = DecelerateInterpolator()
            }
        val short = ObjectAnimator.ofFloat(binding.classicTopFrame, "scaleY", 0.38f).apply {
            duration = 400
            interpolator = LinearInterpolator()
            // для scale необходимо установить pivot(в атрибутах или здесь,в коде)
        }
        val fadeLastNameMode =
            ObjectAnimator.ofFloat(binding.classicStartModeLastName, "alpha", 0f).apply {
                duration = 300
                interpolator = DecelerateInterpolator()
            }
        val fadeDescriptionMode =
            ObjectAnimator.ofFloat(binding.classicModeDescription, "alpha", 0f).apply {
                duration = 300
                interpolator = DecelerateInterpolator()
            }
        val fadeChangeButton =
            ObjectAnimator.ofFloat(binding.classicStartModeButton, "alpha", 0f).apply {
                duration = 300
                interpolator = DecelerateInterpolator()
            }
        val fadeChangeMode =
            ObjectAnimator.ofFloat(binding.classicStartModeChange, "alpha", 0f).apply {
                duration = 300
                interpolator = DecelerateInterpolator()
            }
//        animShortModeVersion.play(titleSmall).with(short).with(fadeLastNameMode).with(fadeChangeButton).with(fadeChangeMode)
//            .with(fadeDescriptionMode)
        AnimatorSet().apply {
            play(short).with(titleSmall).with(fadeLastNameMode).with(fadeChangeButton).with(fadeChangeMode).with(fadeDescriptionMode)
            start()
        }
    }


    fun setShortMode() {
//        animShortModeVersion.start()
        shortModeSet()
    }

    fun setLongMode() {
//        animLongModeVersion.start()
        longModeSet()
    }

    fun setListener(listener:ModeBarListener){
        this.listener = listener
    }


}