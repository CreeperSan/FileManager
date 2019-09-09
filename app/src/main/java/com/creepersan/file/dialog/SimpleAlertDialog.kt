package com.creepersan.file.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.creepersan.file.R
import com.creepersan.file.common.view_holder.IncludeSimpleDialogActionViewHolder
import com.creepersan.file.common.view_holder.IncludeSimpleDialogTitleViewHolder
import com.creepersan.file.manager.ResourceManager

class SimpleAlertDialog(context:Context) : BaseDialog(context, POSITION_CENTER, ANIMATION_CENTER){
    override fun getLayoutID(): Int = R.layout.dialog_common_simple_alert

    private val mMessageTextView = dialogView.findViewById<TextView>(R.id.dialogCommonSimpleAlertTextView)
    private val mTitleViewHolder = IncludeSimpleDialogTitleViewHolder(dialogView.findViewById(R.id.dialogCommonSimpleAlertTitle))
    private val mActionViewHolder = IncludeSimpleDialogActionViewHolder(dialogView.findViewById(R.id.dialogCommonSimpleAlertAction))

    fun setDialogTitle(title:String):SimpleAlertDialog{
        mTitleViewHolder.setTitle(title)
        return this
    }

    fun setDialogTitle(@StringRes titleStringID:Int):SimpleAlertDialog{
        mTitleViewHolder.setTitle(titleStringID)
        return this
    }

    fun setMessage(message:String):SimpleAlertDialog{
        mMessageTextView.text = message
        return this
    }

    fun setPositiveAction(text:String, listener: View.OnClickListener?){
        mActionViewHolder.setPositiveButton(text, listener)
    }

    fun setNegativeAction(text:String, listener: View.OnClickListener?){
        mActionViewHolder.setNegativeButton(text, listener)
    }

    fun setNeutralAction(text:String, listener: View.OnClickListener?){
        mActionViewHolder.setNeutralButton(text, listener)
    }

    fun setPositiveAction(@StringRes textID:Int, listener: View.OnClickListener?){
        mActionViewHolder.setPositiveButton(ResourceManager.getString(textID), listener)
    }

    fun setNegativeAction(@StringRes textID:Int, listener: View.OnClickListener?){
        mActionViewHolder.setNegativeButton(ResourceManager.getString(textID), listener)
    }

    fun setNeutralAction(@StringRes textID:Int, listener: View.OnClickListener?){
        mActionViewHolder.setNeutralButton(ResourceManager.getString(textID), listener)
    }
}