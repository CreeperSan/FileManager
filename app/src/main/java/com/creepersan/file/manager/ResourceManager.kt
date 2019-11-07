package com.creepersan.file.manager

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
            "jpg","jpeg","png","bmp","gif","tga","svg","eif"
                -> R.drawable.file_image
            "mp3","wav","wmv","flac"
                -> R.drawable.file_audio
            "mp4","avi","mpeg","rmvb","rm"
                -> R.drawable.file_video
            "txt","md","markdown"
                -> R.drawable.file_text
            "java","kt","js","ts","c","cpp","cs","go","dart","javascript","py"
                -> R.drawable.file_code
            else
                -> R.drawable.file_file
        }
    }

}