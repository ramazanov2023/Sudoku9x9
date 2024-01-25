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

@BindingAdapter("setMistakes")
fun setGameMistakes(textView: TextView, mistakes: Int?) {
    mistakes?.let {
        textView.text =when(mistakes){
            1 -> "1(+2s)"
            2 -> "2(+4s)"
            3 -> "3(Loss)"
            else -> "No mistakes"
        }
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
        textView.text = toTime(it.progressValue)
        Log.e("nnnn","9  progress-${it.progress}  progressValue-${it.progressValue}")
        Log.e("nnnn","10  toTime-${toTime(it.progressValue)}")
        if (it.progress) {
            textView.setTextColor(Color.parseColor("#28B5FE"))
        } else {
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

    var mls = (it % 1000) / 10
    var sec = it / 1000
    var min = 0L
    var newTime = ""

    if (it == 0L) return "--"

    if (sec < 60) {
        newTime = if (mls < 10) {
            "$sec:0$mls"
        } else {
            "$sec:$mls"
        }
    } else {
        min = sec / 60
        sec %= 60
        newTime = when (sec) {
            in 0..9 -> "$min:0$sec:$mls"
            else -> "$min:$sec:$mls"
        }
    }
    return newTime
}

fun Long.convertToTime():String{
    var mls = (this % 1000) / 10
    var sec = this / 1000
    var min = 0L
    var newTime = ""

    if (this == 0L) return "--"

    if (sec < 60) {
        newTime = if (mls < 10) {
            "$sec:0$mls"
        } else {
            "$sec:$mls"
        }
    } else {
        min = sec / 60
        sec %= 60
        newTime = when (sec) {
            in 0..9 -> "$min:0$sec"
            else -> "$min:$sec"
        }
    }
    return newTime
}