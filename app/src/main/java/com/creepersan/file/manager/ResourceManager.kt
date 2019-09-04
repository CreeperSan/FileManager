package com.creepersan.file.manager

import androidx.core.content.ContextCompat
import com.creepersan.file.application.FileApplication

object ResourceManager{
    private val mContext by lazy { FileApplication.getInstance() }

    fun getString(resInt:Int):String{
        return mContext.getString(resInt)
    }

}