package com.creepersan.file.fragment.main.setting

import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.creepersan.file.R
import com.creepersan.file.common.view_holder.BaseViewHolder

class SettingCheckBoxViewHolder(parent:ViewGroup) : BaseViewHolder(R.layout.item_setting_checkbox, parent) {
    private val titleTextView = itemView.findViewById<TextView>(R.id.itemSettingCheckBoxTitle)
    private val descriptionTextView = itemView.findViewById<TextView>(R.id.itemSettingCheckBoxDescription)
    private val iconImageView = itemView.findViewById<ImageView>(R.id.itemSettingCheckBoxIcon)
    private val checkBoxView = itemView.findViewById<CheckBox>(R.id.itemSettingCheckBoxCheckBox)

    fun setText(text:String){
        titleTextView.text = text
    }

    fun setIcon(iconRes:Int){
        iconImageView.setImageResource(iconRes)
    }

    fun setDescription(description:String){
        descriptionTextView.text = description
    }

    fun setCheckBoxCheck(value:Boolean){
        checkBoxView.isChecked = value
    }

    fun setOnClickListener(listener: View.OnClickListener?){
        itemView.setOnClickListener(listener)
    }
}