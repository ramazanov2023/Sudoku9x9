package com.example.sudoku9x9.ui.classic.start

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.forEach
import androidx.fragment.app.DialogFragment
import com.example.sudoku9x9.R


class ClassicLevelSelectionFragment:DialogFragment() {
    private lateinit var fast:FrameLayout
    private lateinit var light:FrameLayout
    private lateinit var hard:FrameLayout
    private lateinit var master:FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root: View = inflater.inflate(R.layout.fragment_level_selection,container,false)
        fast = root.findViewById(R.id.classic_fast_level)
        light = root.findViewById(R.id.classic_light_level)
        hard = root.findViewById(R.id.classic_hard_level)
        master = root.findViewById(R.id.classic_master_level)

        fast.setOnClickListener {
            setResult(1)
        }
        light.setOnClickListener {
            setResult(2)
        }
        hard.setOnClickListener {
            setResult(3)
        }
        master.setOnClickListener {
            setResult(4)
        }

        return root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        WindowCompat.getInsetsController(requireActivity().window, requireActivity().window.decorView)
            .hide(WindowInsetsCompat.Type.navigationBars())
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }

    private fun setResult(id:Int){
        parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(Pair(KEY_RESPONSE,id)))
    }

    companion object{
        val TAG = ClassicLevelSelectionFragment::class.java.simpleName
        val REQUEST_KEY = "ClassicGameLevel"
        val KEY_RESPONSE = "RESPONSE"
    }
}