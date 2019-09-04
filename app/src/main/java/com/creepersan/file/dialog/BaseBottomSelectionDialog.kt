package com.creepersan.file.dialog

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.file.R
import com.creepersan.file.common.view_holder.BaseViewHolder
import com.creepersan.file.manager.TypedValueManager
import java.util.ArrayList

class BaseBottomSelectionDialog(context: Context) : BaseDialog(context, position = POSITION_BOTTOM, animation = ANIMATION_BOTTOM) {
    private val mRecyclerView : RecyclerView = dialogView.findViewById(R.id.dialogBaseBottomSelectionRecyclerView)
    private val mAdapter = BaseBottomSelectionDialogItemAdapter()
    private val mItemList = ArrayList<BaseBottomSelectionDialogItem>()
    private var mItemClickListener : BaseBottomSelectItemClickListener? = null

    override fun getLayoutID(): Int = R.layout.dialog_base_bottom_selection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初始化
        initRecyclerView()
    }

    private fun initRecyclerView(){
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.adapter = mAdapter
    }

    fun setItemList(itemList : ArrayList<BaseBottomSelectionDialogItem>):BaseBottomSelectionDialog{
        mItemList.clear()
        mItemList.addAll(itemList)
        mAdapter.notifyDataSetChanged()
        return this
    }

    fun setItemClickListener(mItemClickListener : BaseBottomSelectItemClickListener?):BaseBottomSelectionDialog{
        this.mItemClickListener = mItemClickListener
        return this
    }

    /**
     * 底部弹窗的子项Adapter
     */
    private inner class BaseBottomSelectionDialogItemAdapter : RecyclerView.Adapter<BaseBottomSelectionDialogViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBottomSelectionDialogViewHolder {
            return BaseBottomSelectionDialogViewHolder(parent)
        }

        override fun getItemCount(): Int {
            return mItemList.size
        }

        override fun onBindViewHolder(holder: BaseBottomSelectionDialogViewHolder, position: Int) {
            val item = mItemList[position]
            holder.setIcon(item.icon, item.iconSize, item.iconTintColor)
            holder.setHintIcon(item.hintIcon, item.hintIconSize, item.hintIconTintColor)
            holder.setTitle(item.title, item.titleTextSize, item.titleTextColor)
            holder.setHint(item.hintText, item.hintTextSize, item.hintTextColor)
            // 点击时间
            holder.itemView.setOnClickListener {
                mItemClickListener?.onItemClick(item.id, item, this@BaseBottomSelectionDialog)
            }
        }

    }
}

interface BaseBottomSelectItemClickListener{
    fun onItemClick(id:Int, item:BaseBottomSelectionDialogItem, dialog:BaseDialog)
}

/**
 * 底部弹窗的子项ViewHolder
 */
private class BaseBottomSelectionDialogViewHolder(parent:ViewGroup) : BaseViewHolder(R.layout.item_base_bottom_selection_dialog, parent){
    private val iconImageView = itemView.findViewById<ImageView>(R.id.itemBaseBottomSelectionDialogIcon)
    private val titleTextView = itemView.findViewById<TextView>(R.id.itemBaseBottomSelectionDialogTitle)
    private val hintTextView = itemView.findViewById<TextView>(R.id.itemBaseBottomSelectionDialogHintText)
    private val hintImageView = itemView.findViewById<ImageView>(R.id.itemBaseBottomSelectionDialogHintIcon)

    fun setIcon(icon:Int, size:Int, color:Int){
        setImageViewParams(iconImageView, icon, size, color)
    }

    fun setTitle(text:String, size:Int, color:Int){
        setHintTextView(titleTextView, text, size, color)
    }

    fun setHint(text:String, size:Int, color:Int){
        if (text.isEmpty()){
            hintTextView.visibility = View.GONE
        }else{
            hintTextView.visibility = View.VISIBLE
            setHintTextView(hintTextView, text, size, color)
        }
    }

    fun setHintIcon(icon:Int, size:Int, color:Int){
        if (icon == 0){
            hintImageView.visibility = View.GONE
        }else{
            hintImageView.visibility = View.VISIBLE
            setImageViewParams(hintImageView, icon, size, color)
        }
    }

    private fun setImageViewParams(imageView:ImageView, icon:Int, size:Int, color:Int){
        val layoutParams = imageView.layoutParams as LinearLayout.LayoutParams
        layoutParams.width = size
        layoutParams.height = size
        imageView.layoutParams = layoutParams
        imageView.setImageResource(icon)
        imageView.imageTintList = ColorStateList.valueOf(color)
    }

    private fun setHintTextView(textView:TextView, text:String, size:Int, color:Int){
        textView.text = text
        textView.setTextColor(color)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size.toFloat())
    }
}

/**
 * 底部弹窗的子项数据
 */
data class BaseBottomSelectionDialogItem(var id:Int, var icon:Int, var title:String,

                                         var iconSize:Int = TypedValueManager.dp2px(24f).toInt(),
                                         @ColorInt var iconTintColor:Int = Color.BLACK,

                                         @ColorInt var titleTextColor:Int = Color.BLACK,
                                         var titleTextSize:Int = TypedValueManager.sp2px(14f).toInt(),

                                         var hintIcon:Int = 0,
                                         var hintIconSize:Int = TypedValueManager.dp2px(16f).toInt(),
                                         @ColorInt var hintIconTintColor:Int = Color.GRAY,

                                         var hintText:String = "",
                                         @ColorInt var hintTextColor:Int = Color.GRAY,
                                         var hintTextSize:Int = TypedValueManager.sp2px(12f).toInt()
)