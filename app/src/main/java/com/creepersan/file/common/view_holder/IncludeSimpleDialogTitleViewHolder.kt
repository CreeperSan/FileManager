package com.creepersan.file.common.view_holder

import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StringRes
import com.creepersan.file.R
import com.creepersan.file.dialog.SimpleAlertDialog
import com.creepersan.file.manager.ResourceManager

class IncludeSimpleDialogTitleViewHolder(rootView: View):BaseIncludeViewHolder(rootView){
    private val mRootLayout = rootView.findViewById<FrameLayout>(R.id.dialogCommonSimpleTitleRootView)
    private val mTitleTextView = rootView.findViewById<TextView>(R.id.dialogCommonSimpleTitleTextView)

    fun setTitle(title:String){
        mTitleTextView.text = title
    }

    fun setTitle(@StringRes titleStringID:Int) {
        mTitleTextView.text = ResourceManager.getString(titleStringID)
    }
}