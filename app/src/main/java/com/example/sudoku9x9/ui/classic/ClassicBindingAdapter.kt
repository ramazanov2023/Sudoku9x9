package com.example.sudoku9x9.ui.classic

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sudoku9x9.data.local.ClassicCard
import com.example.sudoku9x9.ui.classic.start.ClassicStartAdapter

@BindingAdapter("setClassicCards")
fun setClassicCardsList(recycler:RecyclerView,cards:List<ClassicCard>?){
    cards?.let {
        Log.e("eeee","2 - cards.size - ${cards.size}")
        (recycler.adapter as ClassicStartAdapter).submitList(cards)
    }
}

@BindingAdapter("setClassicBestUserAva")
fun setClassicBestUserAvaList(image:ImageView,ava:Int?){
    ava?.let {
        image.setImageResource(ava)
    }
}

@BindingAdapter("setClassicData")
fun setClassicDataList(textView: TextView,data:ClassicCard?){
    data?.let {

    }
}
@BindingAdapter("setTime")
fun convertLongToTime(textView: TextView,time:Long?){
    time?.let {
        var sec = it / 1000
        var min = 0L
        var newTime = ""
        if(sec<60){
            newTime = "$sec"
        }else{
            min = sec/60
//          sec = sec%60
            sec %= 60
            newTime = when(sec){
                in 0..9 -> "$min:0$sec"
                else -> "$min:$sec"
            }
        }
        textView.text = newTime
    }
}

@BindingAdapter("setGames")
fun convertLongToGames(textView: TextView,games:Long?){
    games?.let {
        textView.text = it.toString()
    }
}