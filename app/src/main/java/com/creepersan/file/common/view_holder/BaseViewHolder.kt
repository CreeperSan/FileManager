package com.creepersan.file.common.view_holder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder : RecyclerView.ViewHolder {

    constructor(itemView: View) : super(itemView)
    constructor(context:Context, layoutID:Int) : super(LayoutInflater.from(context).inflate(layoutID, null))
    constructor(layoutID:Int, parent:ViewGroup) : super(LayoutInflater.from(parent.context).inflate(layoutID, parent, false))

}