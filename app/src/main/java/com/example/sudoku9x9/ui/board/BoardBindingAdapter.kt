package com.example.sudoku9x9.ui.board

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sudoku9x9.data.local.ClassicCard
import com.example.sudoku9x9.ui.classic.start.ClassicStartAdapter

@BindingAdapter("inactiveNumber")
fun setInactiveNumber(number: TextView, hideNumber: Int?) {
    hideNumber?.let {
        if (number.text.equals(hideNumber.toString())) {
//            number.setTextColor(Color.parseColor("#333639"))
            number.visibility = View.INVISIBLE
        }
    }
}

@BindingAdapter("hideNumber")
fun hideCompleteNumber(grid: GridLayout, list: List<Int>?) {
    list?.let {
        for(i in 0..8){
            val num = list[i]
            if(num == 0) (grid[i] as TextView).visibility = View.INVISIBLE
            Log.e("vvvv","grid  -  $num")
        }
    }
}

@BindingAdapter("remainNumber")
fun setRemainNumber(grid: GridLayout, list: List<Int>?) {
    list?.let {
        for(i in 0..8){
            val num = list[i]
            var value = num.toString()
            if(num == 0) value = ""
            (grid[i] as TextView).text = value
            Log.e("vvvv","grid  -  $value")
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



