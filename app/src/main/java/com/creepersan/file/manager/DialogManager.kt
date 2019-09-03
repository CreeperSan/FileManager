package com.creepersan.file.manager

import android.view.Gravity
import android.view.View
import com.creepersan.file.dialog.BaseDialog

object DialogManager{
    const val POSITION_CENTER = 0
    const val POSITION_TOP = 1

    fun showDialog(dialogView:View, position:Int= POSITION_CENTER): BaseDialog{
        val dialog = BaseDialog(dialogView)
        val dialogWindow = dialog.window
        when(position){
            POSITION_CENTER -> {
                dialogWindow?.setGravity(Gravity.CENTER)
            }
            POSITION_TOP -> {
                dialogWindow?.setGravity(Gravity.BOTTOM)
            }
        }
        return dialog
    }

}


