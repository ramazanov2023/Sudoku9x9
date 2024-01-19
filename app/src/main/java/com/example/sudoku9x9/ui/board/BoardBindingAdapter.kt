package com.example.sudoku9x9.ui.board

import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sudoku9x9.data.local.ClassicCard
import com.example.sudoku9x9.ui.classic.start.ClassicStartAdapter

@BindingAdapter("inactiveNumber")
fun setInactiveNumber(number: TextView, hideNumber: Int?) {
    hideNumber?.let {
        if (number.text.equals(hideNumber.toString())) {
            number.setTextColor(Color.parseColor("#3E4245"))
        }
    }
}

fun Int.convertToX(): String {
    return when (this) {
        1 -> "X--"
        2 -> "XX-"
        3 -> "XXX"
        else -> "---"
    }
}



