package com.creepersan.file.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.file.R
import com.creepersan.file.common.view_holder.BaseViewHolder
import com.creepersan.file.global.GlobalClipBoard

class MainRightDrawerRecyclerViewAdapter : RecyclerView.Adapter<BaseViewHolder>(),
    GlobalClipBoard.GlobalClipBoardObserver {

    companion object{
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ITEM = 1
    }

    init {
        GlobalClipBoard.subscribe(this)
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0 -> VIEW_TYPE_HEADER
            else -> VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when(viewType){
            VIEW_TYPE_HEADER -> {
                MainRightDrawerHeaderItemView(parent)
            }
            VIEW_TYPE_ITEM -> {
                MainRightDrawerItemView(parent)
            }
            else -> {
                throw Error("类型不存在")
            }
        }
    }

    override fun onClipboardChange() {
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return GlobalClipBoard.getSize() + 1
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when{
            holder is MainRightDrawerHeaderItemView -> {

            }
            holder is MainRightDrawerItemView -> {
                val fileInfo = GlobalClipBoard.getFileInfo(position - 1)
                holder.setIcon(if(fileInfo.isDirectory){ R.drawable.ic_directory_dark_blue }else{ R.drawable.ic_file_grey })
                holder.setTitle(fileInfo.fullName)
                holder.setPath(fileInfo.path)
                holder.setCloseClickListener(View.OnClickListener {
                    GlobalClipBoard.removeFileInfo(fileInfo)
                })
            }
        }
    }

}

/*************************************** ***********************************************/
class MainRightDrawerHeaderItemView(parent: ViewGroup) : BaseViewHolder(R.layout.item_main_right_drawer_header, parent)
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
