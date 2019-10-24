package com.creepersan.file.manager

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.creepersan.file.R
import com.creepersan.file.application.FileApplication
import com.creepersan.file.bean.file.FileInfo

object ResourceManager{
    private val mContext by lazy { FileApplication.getInstance() }

    fun getString(resInt:Int):String{
        return mContext.getString(resInt)
    }

    fun getColor(@ColorRes color:Int):Int{
        return ContextCompat.getColor(mContext, color)
    }

    fun getFileIcon(fileInfo: FileInfo):Int{
        return when(fileInfo.extensionName){
            else -> R.drawable.ic_file_grey
        }
    }

}