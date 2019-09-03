package com.creepersan.file.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View

open class BaseDialog : Dialog{
    protected var dialogView : View? = null

    constructor(context:Context):super(context)
    constructor(dialogView:View):super(dialogView.context){
        this.dialogView = dialogView
    }

    open fun getLayoutID():Int = 0

    init {
        initLayout()
        onLayoutInflated()
    }

    private fun initLayout(){
        layoutInflater
        val layoutID = getLayoutID()
        if (layoutID != 0){
            dialogView = layoutInflater.inflate(layoutID, null)
        }
    }

    open fun onLayoutInflated(){}

}