package com.example.sudoku9x9.ui.board

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.get
import androidx.databinding.BindingAdapter

@BindingAdapter("inactiveNumber")
fun setInactiveNumber(number: TextView, hideNumber: Int?) {
    hideNumber?.let {
        if (number.text.equals(hideNumber.toString())) {
//            number.setTextColor(Color.parseColor("#333639"))
            number.visibility = View.INVISIBLE
        }
    }
}

@BindingAdapter("speedMode")
fun setSpeedGameMode(image: ImageView, speedMode: Boolean?) {
    speedMode?.let {
        /*if (it) {
            image.setColorFilter(Color.parseColor("#28B5FE"))
        }else{
            image.setColorFilter(Color.parseColor("#B6C0C6"))
        }*/
    }
}
@BindingAdapter("selectInputNumber")
fun selectNumber(grid: GridLayout, inputNumber: Int?) {
    inputNumber?.let {
        for(i in 0..8){
            if(it==i){
                (grid[i] as TextView).setTextColor(Color.parseColor("#28B5FE"))
            }else{
                (grid[i] as TextView).setTextColor(Color.parseColor("#B6C0C6"))
            }
            Log.e("vvvv","grid  -  ${it}")
        }
    }

}

@BindingAdapter("hideNumber")
fun hideCompleteNumber(grid: GridLayout, list: List<Int>?) {
    list?.let {
        for(i in 0..8){
            val num = list[i]
            if(num == 0){
                (grid[i] as TextView).visibility = View.INVISIBLE
            }else{
                (grid[i] as TextView).visibility = View.VISIBLE
            }
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



