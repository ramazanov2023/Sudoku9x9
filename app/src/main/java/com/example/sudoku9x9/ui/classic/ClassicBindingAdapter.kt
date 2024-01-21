package com.example.sudoku9x9.ui.classic

import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sudoku9x9.data.local.ClassicCard
import com.example.sudoku9x9.ui.classic.start.ClassicStartAdapter

@BindingAdapter("setClassicCards")
fun setClassicCardsList(recycler: RecyclerView, cards: List<ClassicCard>?) {
    cards?.let {
        Log.e("eeee", "2 - cards.size - ${cards.size}")
        (recycler.adapter as ClassicStartAdapter).submitList(cards)
    }
}

@BindingAdapter("setClassicBestUserAva")
fun setClassicBestUserAvaList(image: ImageView, ava: Int?) {
    ava?.let {
        image.setImageResource(ava)
    }
}

@BindingAdapter("setClassicData")
fun setClassicDataList(textView: TextView, data: ClassicCard?) {
    data?.let {

    }
}

@BindingAdapter("setTime")
fun convertLongToTime(textView: TextView, time: Long?) {
    time?.let {
        textView.text = toTime(it)
    }
}

@BindingAdapter("setGames")
fun convertLongToGames(textView: TextView, games: Long?) {
    games?.let {
        if (it != 0L) {
            textView.text = it.toString()
        } else {
            textView.text = "--"
        }
    }
}

@BindingAdapter("setMeanTimeProgress")
fun setClassicProgressMeanTime(textView: TextView, progress: ClassicCard?) {
    progress?.let {
        val lastMeanTime = it.lastMeanTime
        val meanTime = it.meanTime
        if (lastMeanTime < meanTime) {
            textView.text = toTime(meanTime - lastMeanTime)
            textView.setTextColor(Color.parseColor("#28B5FE"))
        } else {
            textView.text = toTime(lastMeanTime - meanTime)
            textView.setTextColor(Color.parseColor("#F02D63"))
        }
    }
}

@BindingAdapter("setBestTimeProgress")
fun setClassicProgressBestTime(textView: TextView, progress: ClassicCard?) {
    progress?.let {
        val lastTime = it.lastTime
        val pastBesTime = it.pastBesTime
        if (lastTime < pastBesTime) {
            textView.text = toTime(pastBesTime - lastTime)
            textView.setTextColor(Color.parseColor("#28B5FE"))
        } else {
            textView.text = toTime(lastTime - pastBesTime)
            textView.setTextColor(Color.parseColor("#F02D63"))
        }
    }
}


private fun toTime(it: Long): String {

    var sec = it / 1000
    var min = 0L
    var newTime = ""

    if (it == 0L) return "--"

    if (sec < 60) {
        newTime = "$sec"
    } else {
        min = sec / 60
//          sec = sec%60
        sec %= 60
        newTime = when (sec) {
            in 0..9 -> "$min:0$sec"
            else -> "$min:$sec"
        }
    }
    return newTime
}