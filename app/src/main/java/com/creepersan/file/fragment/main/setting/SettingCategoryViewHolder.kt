package com.creepersan.file.fragment.main.setting

import android.view.ViewGroup
import android.widget.TextView
import com.creepersan.file.R
import com.creepersan.file.common.view_holder.BaseViewHolder

class SettingCategoryViewHolder(parent:ViewGroup) : BaseViewHolder(R.layout.item_setting_category, parent) {
    private val titleTextView = itemView.findViewById<TextView>(R.id.itemSettingCategoryTitleTextView)

    fun setText(text:String){
        titleTextView.text = text
    }

}