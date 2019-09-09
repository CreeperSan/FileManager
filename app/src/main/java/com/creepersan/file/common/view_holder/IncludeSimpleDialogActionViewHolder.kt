package com.creepersan.file.common.view_holder

import android.view.View
import android.widget.Button
import androidx.annotation.StringRes
import com.creepersan.file.R
import com.creepersan.file.extension.gone
import com.creepersan.file.extension.visible
import com.creepersan.file.manager.ResourceManager

class IncludeSimpleDialogActionViewHolder(rootView: View) : BaseViewHolder(rootView) {
    private val mPositiveButton = rootView.findViewById<Button>(R.id.includeDialogCommonSimplePositiveAction)
    private val mNegativeButton = rootView.findViewById<Button>(R.id.includeDialogCommonSimpleNegativeAction)
    private val mNeutralButton = rootView.findViewById<Button>(R.id.includeDialogCommonSimpleNeutralAction)

    fun setPositiveButton(text:String, clickListener:View.OnClickListener?){
        setButton(mPositiveButton, text, clickListener)
    }

    fun setNegativeButton(text:String, clickListener:View.OnClickListener?){
        setButton(mNegativeButton, text, clickListener)
    }

    fun setNeutralButton(text:String, clickListener:View.OnClickListener?){
        setButton(mNeutralButton, text, clickListener)
    }

    fun setPositiveButton(@StringRes textID:Int, clickListener:View.OnClickListener?){
        setButton(mPositiveButton, ResourceManager.getString(textID), clickListener)
    }

    fun setNegativeButton(@StringRes textID:Int, clickListener:View.OnClickListener?){
        setButton(mNegativeButton, ResourceManager.getString(textID), clickListener)
    }

    fun setNeutralButton(@StringRes textID:Int, clickListener:View.OnClickListener?){
        setButton(mNeutralButton, ResourceManager.getString(textID), clickListener)
    }

    private fun setButton(button:Button, text:String, clickListener: View.OnClickListener?){
        if (text.isEmpty() || clickListener == null){
            button.gone()
        }else{
            button.visible()
            button.text = text
            button.setOnClickListener(clickListener)
        }
    }

}