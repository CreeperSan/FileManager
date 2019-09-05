package com.creepersan.file.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.file.R
import com.creepersan.file.common.view_holder.BaseViewHolder
import com.creepersan.file.global.GlobalClipBoard

class MainRightDrawerRecyclerViewAdapter : RecyclerView.Adapter<MainRightDrawerItemView>(),
    GlobalClipBoard.GlobalClipBoardObserver {

    init {
        GlobalClipBoard.subscribe(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRightDrawerItemView {
        return MainRightDrawerItemView(parent)
    }

    override fun onClipboardChange() {
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return GlobalClipBoard.getSize()
    }

    override fun onBindViewHolder(holder: MainRightDrawerItemView, position: Int) {
        val fileInfo = GlobalClipBoard.getFileInfo(position)
        holder.setIcon(if(fileInfo.isDirectory){ R.drawable.ic_directory_dark_blue }else{ R.drawable.ic_file_grey })
        holder.setTitle(fileInfo.fullName)
        holder.setPath(fileInfo.path)
        holder.setCloseClickListener(View.OnClickListener {
            GlobalClipBoard.removeFileInfo(fileInfo)
        })
    }

}

/*************************************** ***********************************************/
class MainRightDrawerItemView(parent: ViewGroup) : BaseViewHolder(R.layout.item_main_right_drawer_item, parent){
    private val iconImageView = itemView.findViewById<ImageView>(R.id.itemMainRightDrawerItemIcon)
    private val titleTextView = itemView.findViewById<TextView>(R.id.itemMainRightDrawerItemNameTextView)
    private val pathTextView = itemView.findViewById<TextView>(R.id.itemMainRightDrawerItemPathTextView)
    private val closeImageView = itemView.findViewById<ImageView>(R.id.itemMainRightDrawerItemCloseImageView)

    fun setIcon(iconRes:Int){
        iconImageView.setImageResource(iconRes)
    }

    fun setTitle(title:String){
        titleTextView.text = title
    }

    fun setPath(path:String){
        pathTextView.text = path
    }

    fun setCloseClickListener(listener: View.OnClickListener?){
        closeImageView.setOnClickListener(listener)
    }

}
