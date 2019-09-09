package com.creepersan.file.manager

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.creepersan.file.application.FileApplication

object ResourceManager{
    private val mContext by lazy { FileApplication.getInstance() }

    fun getString(resInt:Int):String{
        return mContext.getString(resInt)
    }

    fun getColor(@ColorRes color:Int):Int{
        return ContextCompat.getColor(mContext, color)
    }

}