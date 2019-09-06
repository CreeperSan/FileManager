package com.creepersan.file.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.file.R
import com.creepersan.file.activity.MainActivity
import com.creepersan.file.activity.MainFragmentListObserver
import com.creepersan.file.application.FileApplication
import com.creepersan.file.common.view_holder.BaseViewHolder
import com.creepersan.file.fragment.main.BaseMainFragment
import java.util.ArrayList

class MainLeftDrawerRecyclerViewAdapter(
    private val mainFragmentPagerAdapter: MainFragmentPagerAdapter,
    private val observer: MainFragmentListObserver,
    private val mainActivityController:MainActivity.Controller
) : RecyclerView.Adapter<BaseViewHolder>(),
    MainFragmentListObserver.Subscriber {

    companion object{
        private const val VIEW_TYPE_UNDEFINE = -1
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_TITLE = 1
        private const val VIEW_TYPE_SELECTION = 2
        private const val VIEW_TYPE_SELECTION_CLOSABLE = 3
        private const val VIEW_TYPE_APP_SELECTION = 4
    }

    init {
        observer.subscribe(this)
    }

    private val mItemList = ArrayList<BaseMainLeftDrawerItem>()

    override fun onListChange(fragmentList: ArrayList<BaseMainFragment>) {
        mItemList.clear()
        mItemList.add(MainLeftDrawerHeaderItem())
        mItemList.add(MainLeftDrawerTitleItem("已打开的窗口"))
        for (i in 0 until observer.getSize()){
            observer.getFragment(i).apply {
                mItemList.add(MainLeftDrawerSelectionItem(
                    this.getIcon(),
                    this.getName(),
                    true
                ))
                Log.e("TAG", "AddOne")
            }
        }
        mItemList.add(MainLeftDrawerTitleItem("应用程序"))
        mItemList.add(MainLeftDrawerAppSelectionItem(R.drawable.ic_setting, "设置", View.OnClickListener {

        }))
        mItemList.add(MainLeftDrawerAppSelectionItem(R.drawable.ic_info_outline, "关于", View.OnClickListener {

        }))
        mItemList.add(MainLeftDrawerAppSelectionItem(R.drawable.ic_exit, "退出", View.OnClickListener {
            FileApplication.getInstance().exit()
        }))
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val item = mItemList[position]
        return when{
            item is MainLeftDrawerHeaderItem -> VIEW_TYPE_HEADER
            item is MainLeftDrawerTitleItem -> VIEW_TYPE_TITLE
            item is MainLeftDrawerSelectionItem && item.isClosable -> VIEW_TYPE_SELECTION_CLOSABLE
            item is MainLeftDrawerSelectionItem && !item.isClosable -> VIEW_TYPE_SELECTION
            item is MainLeftDrawerAppSelectionItem -> VIEW_TYPE_APP_SELECTION
            else -> VIEW_TYPE_UNDEFINE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when(viewType){
            VIEW_TYPE_SELECTION_CLOSABLE -> MainLeftDrawerPagerItemView(parent)
            VIEW_TYPE_SELECTION -> MainLeftDrawerSelectionItemView(parent)
            VIEW_TYPE_TITLE -> MainLeftDrawerTitleItemView(parent)
            VIEW_TYPE_HEADER -> MainLeftDrawerHeaderItemView(parent)
            VIEW_TYPE_APP_SELECTION -> MainLeftDrawerAppSelectionItemView(parent)
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
            holder is MainLeftDrawerPagerItemView -> {
                val fragmentPosition = holder.adapterPosition - 2
                val item = mItemList.get(position) as MainLeftDrawerSelectionItem
                holder.setTitle(item.title)
                holder.setIcon(item.icon)
                holder.setOnClickListener(View.OnClickListener {
                    mainActivityController.setCurrentPage(fragmentPosition)
                    mainActivityController.closeLeftDrawer()
                })
                holder.setOnCloseClickListener(View.OnClickListener {
                    mainActivityController.closePage(fragmentPosition) // 减2是因为
                })
            }
            holder is MainLeftDrawerAppSelectionItemView -> {
                val item = mItemList.get(position) as MainLeftDrawerAppSelectionItem
                holder.setIcon(item.iconID)
                holder.setTitle(item.title)
                holder.setOnClickListener(item.clickListener)
            }
        }
    }

}


/******************************** ItemBean ********************************************************/
open class BaseMainLeftDrawerItem
class MainLeftDrawerHeaderItem : BaseMainLeftDrawerItem()
class MainLeftDrawerTitleItem(var title:String) : BaseMainLeftDrawerItem()
class MainLeftDrawerSelectionItem(var icon:Int, var title:String, val isClosable:Boolean=false) : BaseMainLeftDrawerItem()
class MainLeftDrawerAppSelectionItem(var iconID: Int, var title:String, var clickListener: View.OnClickListener) : BaseMainLeftDrawerItem()

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
class MainLeftDrawerPagerItemView(viewGroup: ViewGroup) : BaseViewHolder(R.layout.item_main_left_drawer_selection_closable, viewGroup) {
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
class MainLeftDrawerAppSelectionItemView(viewGroup: ViewGroup) : BaseViewHolder(R.layout.item_main_left_drawer_app_selection, viewGroup){
    private val iconImageView = itemView.findViewById<ImageView>(R.id.itemMainLeftDrawerAppSelectionIcon)
    private val titleTextView = itemView.findViewById<TextView>(R.id.itemMainLeftDrawerAppSelectionTitle)

    fun setIcon(iconID:Int){
        iconImageView.setImageResource(iconID)
    }

    fun setTitle(title:String){
        titleTextView.text = title
    }

    fun setOnClickListener(listener:View.OnClickListener){
        itemView.setOnClickListener(listener)
    }
}