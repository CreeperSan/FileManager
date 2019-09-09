package com.creepersan.file.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.file.R
import com.creepersan.file.common.view_holder.BaseViewHolder
import com.creepersan.file.extension.gone
import com.creepersan.file.extension.visible
import com.creepersan.file.global.GlobalFileInfoClipBoard

class MainRightDrawerRecyclerViewAdapter : RecyclerView.Adapter<MainRightDrawerItemView>(),
    GlobalFileInfoClipBoard.GlobalClipBoardObserver {

    init {
        GlobalFileInfoClipBoard.subscribe(this)
    }

    fun destroy(){
        GlobalFileInfoClipBoard.unsubscribe(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRightDrawerItemView {
        return MainRightDrawerItemView(parent)
    }

    override fun onClipboardChange() {
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return GlobalFileInfoClipBoard.getSize()
    }

    override fun onBindViewHolder(holder: MainRightDrawerItemView, position: Int) {
        val copiedFileInfo = GlobalFileInfoClipBoard.getCopiedFileInfo(position)
        val fileInfo = copiedFileInfo.fileInfo
        holder.setIcon(if(fileInfo.isDirectory){ R.drawable.ic_directory_dark_blue }else{ R.drawable.ic_file_grey })
        holder.setTitle(fileInfo.fullName)
        holder.setPath(fileInfo.path)
        when(copiedFileInfo.action){
            GlobalFileInfoClipBoard.ACTION_CUT -> {
                holder.showCover()
                holder.setCoverIcon(R.drawable.ic_content_cut)
            }
            GlobalFileInfoClipBoard.ACTION_COPY -> {
                holder.hideCover()
            }
            else -> {
                holder.hideCover()
            }
        }
        holder.setCloseClickListener(View.OnClickListener {
            GlobalFileInfoClipBoard.removeFileInfo(fileInfo)
        })
    }

}

/*************************************** ***********************************************/
class MainRightDrawerItemView(parent: ViewGroup) : BaseViewHolder(R.layout.item_main_right_drawer_item, parent){
    private val iconImageView = itemView.findViewById<ImageView>(R.id.itemMainRightDrawerItemIcon)
    private val iconCoverImageView = itemView.findViewById<ImageView>(R.id.itemMainRightDrawerItemIconCoverImage)
    private val titleTextView = itemView.findViewById<TextView>(R.id.itemMainRightDrawerItemNameTextView)
    private val pathTextView = itemView.findViewById<TextView>(R.id.itemMainRightDrawerItemPathTextView)
    private val closeImageView = itemView.findViewById<ImageView>(R.id.itemMainRightDrawerItemCloseImageView)

    fun setIcon(iconRes:Int){
        iconImageView.setImageResource(iconRes)
    }

    fun setCoverIcon(res:Int){
        iconCoverImageView.setImageResource(res)
    }

    fun showCover(){
        iconCoverImageView.visible()
    }

    fun hideCover(){
        iconCoverImageView.gone()
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
