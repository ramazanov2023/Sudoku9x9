package com.example.sudoku9x9.ui.profile

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("setDate")
fun setSignUpDate(text: TextView, date: Long?) {
    date?.let {
        val signUpDate = Date(it)
        val formatDate = SimpleDateFormat("EEE, MMM d", Locale.ENGLISH)
        text.text = formatDate.format(signUpDate)
    }
}

@BindingAdapter("setAvatar")
fun setUserAvatar(image: ImageView, avatarUri: String?) {
    Log.e("oooo","3 ")
    avatarUri?.let {
        Log.e("oooo","5 - avatarUri-$it")
        Glide
            .with(image.context)
            .load(avatarUri)
            .fitCenter()
            .into(image)
    }
}

@BindingAdapter("setAppVersion")
fun setUserAppVersion(text: TextView, pro: Boolean?) {
    pro?.let {
        text.text = if(it){
            "Pro"
        }else{
            "Free"
        }
    }
}

@BindingAdapter("setSignIn")
fun setUserSignIn(text: TextView, signIn: Boolean?) {
    signIn?.let {
        text.text = if(it){
            "Online"
        }else{
            "Offline"
        }
    }
}

@BindingAdapter("setSignUp")
fun setUserSignUp(text: TextView, signUp: Boolean?) {
    signUp?.let {
        text.text = if(it){
            "Yes"
        }else{
            "No"
        }
    }
}




