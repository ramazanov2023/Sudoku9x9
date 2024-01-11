package com.example.sudoku9x9.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sudoku9x9.MainActivity
import com.example.sudoku9x9.databinding.FragmentClassicStartBinding
import com.example.sudoku9x9.databinding.FragmentProfileBinding

class ProfileFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProfileBinding.inflate(inflater)

//        (activity as? AppCompatActivity)?.supportActionBar?.title = "Profile"
//        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        (activity as MainActivity).bottomNavBar.visibility = View.INVISIBLE


        return binding.root
    }


}