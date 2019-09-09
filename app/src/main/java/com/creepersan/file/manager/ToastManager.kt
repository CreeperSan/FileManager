package com.creepersan.file.manager

import android.widget.Toast
import androidx.annotation.StringRes
import com.creepersan.file.application.FileApplication

object ToastManager{

    fun show(@StringRes content:Int, isShort:Boolean=true){
        show(ResourceManager.getString(content), isShort)
    }

    fun show(content:String, isShort:Boolean=true){
        Toast.makeText(FileApplication.getInstance(), content, if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG ).show()
    }

}