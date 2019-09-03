package com.creepersan.file.manager

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import com.creepersan.file.application.FileApplication

object TypedValueManager {
    private val mDisplayMetrics = FileApplication.getInstance().baseContext.resources.displayMetrics

    fun sp2px(sp:Float):Float{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, mDisplayMetrics)
    }

    fun dp2px(dp:Float):Float{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mDisplayMetrics)
    }

}