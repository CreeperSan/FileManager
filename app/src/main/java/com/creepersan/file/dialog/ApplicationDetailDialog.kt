package com.creepersan.file.dialog

import android.content.Context
import android.os.Bundle
import com.creepersan.file.R

class ApplicationDetailDialog(context: Context) : BaseDialog(context, POSITION_CENTER, ANIMATION_CENTER){
    override fun getLayoutID(): Int = R.layout.dialog_application_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}