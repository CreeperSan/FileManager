package com.creepersan.file.extension

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.File

fun View.visible(){
    this.visibility = View.VISIBLE
}

fun View.invisible(){
    this.visibility = View.INVISIBLE
}

fun View.gone(){
    this.visibility = View.GONE
}

fun ImageView.glide(filePath:String){
    Glide.with(this).load(File(filePath)).into(this)
}