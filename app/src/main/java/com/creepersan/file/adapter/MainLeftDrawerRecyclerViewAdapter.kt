package com.creepersan.file.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.file.R
import com.creepersan.file.common.view_holder.BaseViewHolder
import java.util.ArrayList

class MainLeftDrawerRecyclerViewAdapter(private val mainFragmentPagerAdapter: MainFragmentPagerAdapter) : RecyclerView.Adapter<BaseViewHolder>(){
    companion object{
        private const val VIEW_TYPE_UNDEFINE = -1
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_TITLE = 1
        private const val VIEW_TYPE_SELECTION = 2
        private const val VIEW_TYPE_SELECTION_CLOSABLE = 3
    }

    private val mItemList = ArrayList<BaseMainLeftDrawerItem>()

    fun notifyFragmentListChange(){
        notifyDataSetChanged()
        mItemList.clear()
        mItemList.add(MainLeftDrawerHeaderItem())
        mItemList.add(MainLeftDrawerTitleItem("已打开的窗口"))
        Log.e("TAG", "${mainFragmentPagerAdapter.getFragmentSize()}")
        for (i in 0 until mainFragmentPagerAdapter.getFragmentSize()){
            Log.e("TAG", "findOne")
            mainFragmentPagerAdapter.getFragment(i)?.apply {
                mItemList.add(MainLeftDrawerSelectionItem(
                    this.getIcon(),
                    this.getName(),
                    true
                ))
                Log.e("TAG", "AddOne")
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val item = mItemList[position]
        return when{
            item is MainLeftDrawerHeaderItem -> VIEW_TYPE_HEADER
            item is MainLeftDrawerTitleItem -> VIEW_TYPE_TITLE
            item is MainLeftDrawerSelectionItem && item.isClosable -> VIEW_TYPE_SELECTION_CLOSABLE
            item is MainLeftDrawerSelectionItem && !item.isClosable -> VIEW_TYPE_SELECTION
            else -> VIEW_TYPE_UNDEFINE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when(viewType){
            VIEW_TYPE_SELECTION_CLOSABLE -> MainLeftDrawerSelectionClosableItemView(parent)
            VIEW_TYPE_SELECTION -> MainLeftDrawerSelectionItemView(parent)
            VIEW_TYPE_TITLE -> MainLeftDrawerTitleItemView(parent)
            VIEW_TYPE_HEADER -> MainLeftDrawerHeaderItemView(parent)
            else -> throw Error("未知类型")
        }
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when{
            holder is MainLeftDrawerHeaderItemView -> {
                val item = mItemList.get(position) as MainLeftDrawerHeaderItem
            }
            holder is MainLeftDrawerTitleItemView -> {
                val item = mItemList.get(position) as MainLeftDrawerTitleItem
                holder.setTitle(item.title)
            }
            holder is MainLeftDrawerSelectionItemView -> {
                val item = mItemList.get(position) as MainLeftDrawerSelectionItem
                holder.setTitle(item.title)
                holder.setIcon(item.icon)
                holder.setOnClickListener(View.OnClickListener {

                })
            }
            holder is MainLeftDrawerSelectionClosableItemView -> {
                val item = mItemList.get(position) as MainLeftDrawerSelectionItem
                holder.setTitle(item.title)
                holder.setIcon(item.icon)
                holder.setOnClickListener(View.OnClickListener {

                })
                holder.setOnClickListener(View.OnClickListener {

                })
            }
        }
    }

}


/******************************** ItemBean ********************************************************/
open class BaseMainLeftDrawerItem
class MainLeftDrawerHeaderItem : BaseMainLeftDrawerItem()
class MainLeftDrawerTitleItem(var title:String) : BaseMainLeftDrawerItem()
class MainLeftDrawerSelectionItem(var icon:Int, var title:String, val isClosable:Boolean=false) : BaseMainLeftDrawerItem()

/******************************** ViewHolder ******************************************************/
class MainLeftDrawerHeaderItemView(viewGroup: ViewGroup) : BaseViewHolder(R.layout.item_main_left_drawer_header, viewGroup) {
    private val headerImageView = itemView.findViewById<ImageView>(R.id.itemMainLeftDrawerHeaderImage)

    fun setImageView(resID:Int){
        headerImageView.setImageResource(resID)
    }
}

class MainLeftDrawerTitleItemView(viewGroup: ViewGroup) : BaseViewHolder(R.layout.item_main_left_drawer_title, viewGroup) {
    private val titleTextView = itemView.findViewById<TextView>(R.id.itemMainLeftDrawerTitleTitleTextView)

    fun setTitle(title:String){
        titleTextView.text = title
    }
}

class MainLeftDrawerSelectionItemView(viewGroup: ViewGroup) : BaseViewHolder(R.layout.item_main_left_drawer_selection, viewGroup) {
    private val iconImageView = itemView.findViewById<ImageView>(R.id.itemMainLeftDrawerSelectionIcon)
    private val titleTextView = itemView.findViewById<TextView>(R.id.itemMainLeftDrawerSelectionTitle)

    fun setIcon(resID: Int){
        iconImageView.setImageResource(resID)
    }

    fun setTitle(title:String){
        titleTextView.text = title
    }

    fun setOnClickListener(listener : View.OnClickListener){
        itemView.setOnClickListener(listener)
    }
}

class MainLeftDrawerSelectionClosableItemView(viewGroup: ViewGroup) : BaseViewHolder(R.layout.item_main_left_drawer_selection_closable, viewGroup) {
    private val iconImageView = itemView.findViewById<ImageView>(R.id.itemMainLeftDrawerClosableSelectionIcon)
    private val titleTextView = itemView.findViewById<TextView>(R.id.itemMainLeftDrawerClosableSelectionTitle)
    private val closeImageView = itemView.findViewById<ImageView>(R.id.itemMainLeftDrawerClosableSelectionClose)

    fun setIcon(resID: Int){
        iconImageView.setImageResource(resID)
    }

    fun setTitle(title:String){
        titleTextView.text = title
    }

    fun setOnClickListener(listener : View.OnClickListener){
        itemView.setOnClickListener(listener)
    }

    fun setOnCloseClickListener(listener : View.OnClickListener){
        closeImageView.setOnClickListener(listener)
    }
}