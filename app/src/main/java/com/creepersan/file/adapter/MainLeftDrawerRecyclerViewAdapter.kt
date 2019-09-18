package com.creepersan.file.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.file.R
import com.creepersan.file.activity.FragmentPageObserver
import com.creepersan.file.activity.FragmentPagerSubscriber
import com.creepersan.file.activity.MainActivity
import com.creepersan.file.application.FileApplication
import com.creepersan.file.common.view_holder.BaseViewHolder
import com.creepersan.file.fragment.main.BaseMainFragment
import java.lang.Exception
import java.util.ArrayList

class MainLeftDrawerRecyclerViewAdapter(
    private val observer: FragmentPageObserver,
    private val mainActivityController:MainActivity.Controller
) : RecyclerView.Adapter<BaseViewHolder>(),
    FragmentPagerSubscriber {

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

    override fun onPageUpdate(index: Int, observer: FragmentPageObserver) {
        notifyItemChanged(mItemList.windowIndexToGeneralIndex(index))
    }

    override fun onPageChange(observer: FragmentPageObserver) {
        mItemList.clearWindowItem()
        for (i in 0 until observer.getSize()){
            val fragment = observer.getFragment(i)
            mItemList.addOpenedWindowItem(MainLeftDrawerSelectionItem(
                fragment.getIcon(),
                fragment.getName(),
                true
            ))
        }
        notifyDataSetChanged()
    }

    fun initBaseData(){
        mItemList.addOpenedWindowTopItem(MainLeftDrawerHeaderItem())
        mItemList.addOpenedWindowTopItem(MainLeftDrawerTitleItem("已打开的窗口"))
        mItemList.addOpenedWindowBottomItem(MainLeftDrawerTitleItem("应用程序"))
        mItemList.addOpenedWindowBottomItem(MainLeftDrawerAppSelectionItem(R.drawable.ic_setting, "设置", View.OnClickListener {

        }))
        mItemList.addOpenedWindowBottomItem(MainLeftDrawerAppSelectionItem(R.drawable.ic_info_outline, "关于", View.OnClickListener {

        }))
        mItemList.addOpenedWindowBottomItem(MainLeftDrawerAppSelectionItem(R.drawable.ic_exit, "退出", View.OnClickListener {
            FileApplication.getInstance().exit()
        }))
    }

    fun destroy(){
        observer.unsubscribe(this)
    }

    private val mItemList = DrawerItemList()

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
        return mItemList.size()
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when{
            holder is MainLeftDrawerHeaderItemView -> {
                val item = mItemList[position] as MainLeftDrawerHeaderItem
            }
            holder is MainLeftDrawerTitleItemView -> {
                val item = mItemList[position] as MainLeftDrawerTitleItem
                holder.setTitle(item.title)
            }
            holder is MainLeftDrawerSelectionItemView -> {
                val item = mItemList[position] as MainLeftDrawerSelectionItem
                holder.setTitle(item.title)
                holder.setIcon(item.icon)
                holder.setOnClickListener(View.OnClickListener {

                })
            }
            holder is MainLeftDrawerPagerItemView -> {
                val fragmentPosition = mItemList.generalIndexToFragmentIndex(position)
                val item = mItemList.get(position) as MainLeftDrawerSelectionItem
                try {
                    item.title = observer.getFragment(mItemList.generalIndexToFragmentIndex(position)).getName()
                }catch (e:Exception){
                    e.printStackTrace()
                }
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

class DrawerItemList{
    private val mTopItemList = ArrayList<BaseMainLeftDrawerItem>() // 用于储存一打开窗口内容上面的
    private val mOpenedWindowItemList = ArrayList<BaseMainLeftDrawerItem>() // 用于储存已打开窗口子项
    private val mBottomItemList = ArrayList<BaseMainLeftDrawerItem>() // 用于储存已打开窗口子项内容下面的

    fun clear(){
        mTopItemList.clear()
        mOpenedWindowItemList.clear()
        mBottomItemList.clear()
    }

    fun windowIndexToGeneralIndex(windowIndex:Int):Int{
        return windowIndex + mTopItemList.size
    }

    fun generalIndexToFragmentIndex(generalIndex:Int):Int{
        return generalIndex - mTopItemList.size
    }

    fun clearWindowItem(){
        mOpenedWindowItemList.clear()
    }

    fun addOpenedWindowTopItem(item:BaseMainLeftDrawerItem){
        mTopItemList.add(item)
    }

    fun addOpenedWindowItem(item:BaseMainLeftDrawerItem){
        mOpenedWindowItemList.add(item)
    }

    fun addOpenedWindowBottomItem(item:BaseMainLeftDrawerItem){
        mBottomItemList.add(item)
    }

    operator fun get(index:Int):BaseMainLeftDrawerItem{
        val topSize = mTopItemList.size
        val windowSize = mOpenedWindowItemList.size
        val bottomSize = mBottomItemList.size
        return when{
            index < topSize -> mTopItemList[index]
            index < topSize + windowSize -> mOpenedWindowItemList[index - topSize]
            index < topSize + windowSize + bottomSize-> mBottomItemList[index - topSize - windowSize]
            else -> throw IndexOutOfBoundsException("DrawerItemList下标越界 index=$index size=${size()}")
        }
    }

    fun size():Int{
        return mTopItemList.size + mOpenedWindowItemList.size + mBottomItemList.size
    }

}